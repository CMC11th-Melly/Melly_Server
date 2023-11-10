package cmc.mellyserver.domain.scrap.integration;

import static org.assertj.core.api.Assertions.*;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;

import cmc.mellyserver.dbcore.place.Place;
import cmc.mellyserver.dbcore.place.PlaceRepository;
import cmc.mellyserver.dbcore.scrap.PlaceScrap;
import cmc.mellyserver.dbcore.scrap.PlaceScrapRepository;
import cmc.mellyserver.dbcore.scrap.enums.ScrapType;
import cmc.mellyserver.dbcore.user.User;
import cmc.mellyserver.dbcore.user.UserRepository;
import cmc.mellyserver.domain.scrap.PlaceScrapService;
import cmc.mellyserver.domain.scrap.dto.request.CreatePlaceScrapRequestDto;
import cmc.mellyserver.domain.scrap.dto.response.ScrapedPlaceListResponse;
import cmc.mellyserver.fixtures.PlaceFixtures;
import cmc.mellyserver.fixtures.UserFixtures;
import cmc.mellyserver.support.IntegrationTestSupport;
import cmc.mellyserver.support.exception.BusinessException;
import cmc.mellyserver.support.exception.ErrorCode;

public class PlaceScrapServiceTest extends IntegrationTestSupport {

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private PlaceRepository placeRepository;

	@Autowired
	private PlaceScrapRepository placeScrapRepository;

	@Autowired
	private PlaceScrapService placeScrapService;

	@DisplayName("사용자가 스크랩한 장소를 조회한다")
	@Test
	void 사용자가_스크랩한_장소를_조회한다() {

		// Given
		Place 스타벅스 = placeRepository.save(PlaceFixtures.스타벅스());
		User 모카 = userRepository.save(UserFixtures.모카());

		// When
		placeScrapService.createScrap(
			new CreatePlaceScrapRequestDto(모카.getId(), 스타벅스.getPosition().getLat(), 스타벅스.getPosition().getLng(),
				ScrapType.FRIEND, 스타벅스.getPlaceName(), 스타벅스.getPlaceCategory()));

		// Then
		ScrapedPlaceListResponse scrapedPlace = placeScrapService.findScrapedPlace(-1L, PageRequest.of(0, 10),
			모카.getId(), ScrapType.FRIEND);

		// then
		assertThat(scrapedPlace.getContents()).hasSize(1);
	}

	@DisplayName("스크랩을 추가할때")
	@Nested
	class 스크랩을_추가할때 {

		@DisplayName("이미 스크랩을 한 상태라면 중복 예외가 발생한다")
		@Test
		void 이미_스크랩을_한_상태라면_예외가_발생한다() {

			// Given
			Place 스타벅스 = placeRepository.save(PlaceFixtures.스타벅스());
			User 모카 = userRepository.save(UserFixtures.모카());

			// When
			CreatePlaceScrapRequestDto createPlaceScrapRequestDto = new CreatePlaceScrapRequestDto(모카.getId(),
				스타벅스.getPosition().getLat(), 스타벅스.getPosition().getLng(), ScrapType.FRIEND, 스타벅스.getPlaceName(),
				스타벅스.getPlaceCategory());
			placeScrapService.createScrap(createPlaceScrapRequestDto);

			// then
			assertThatThrownBy(() -> placeScrapService.createScrap(createPlaceScrapRequestDto))
				.isInstanceOf(BusinessException.class)
				.hasMessage(ErrorCode.DUPLICATE_SCRAP.getMessage());
		}

		@DisplayName("[AOP 동작 테스트] 스크랩하려는 장소에 DB에 없으면 스크랩과 함께 장소도 추가한다")
		@Test
		void 스크랩하려는_장소가_DB에_없으면_장소를_추가한다() {

			// Given
			User 모카 = userRepository.save(UserFixtures.모카());

			CreatePlaceScrapRequestDto createPlaceScrapRequestDto = new CreatePlaceScrapRequestDto(모카.getId(), 1.555,
				1.555,
				ScrapType.FRIEND, "스타벅스", "카페");

			// when
			placeScrapService.createScrap(createPlaceScrapRequestDto);

			// then
			Optional<Place> place = placeRepository.findByPosition(createPlaceScrapRequestDto.getPosition());
			assertThat(place).isPresent();
		}
	}

	@DisplayName("스크랩을 삭제 하려고 할때")
	@Nested
	class 스크랩을_삭제하려고_할때 {

		@DisplayName("스크랩이 존재한다면 스크랩을 삭제할 수 있다.")
		@Test
		void 스크랩이_존재한다면_삭제_가능하다() {

			// Given
			Place 스타벅스 = placeRepository.save(PlaceFixtures.스타벅스());
			User 모카 = userRepository.save(UserFixtures.모카());

			// When
			placeScrapService.createScrap(
				new CreatePlaceScrapRequestDto(모카.getId(), 스타벅스.getPosition().getLat(), 스타벅스.getPosition().getLng(),
					ScrapType.FRIEND, 스타벅스.getPlaceName(), 스타벅스.getPlaceCategory()));

			// when
			placeScrapService.removeScrap(모카.getId(), 스타벅스.getPosition());

			// then
			List<PlaceScrap> placeScraps = placeScrapRepository.findAll();
			assertThat(placeScraps).isEmpty();

		}

		@DisplayName("기존에 스크랩이 존재하지 않는다면 삭제하지 못하고 예외가 발생한다.")
		@Test
		void 스크랩이_존재하지않는다면_삭제_불가능하다() {

			// Given
			Place 스타벅스 = placeRepository.save(PlaceFixtures.스타벅스());
			User 모카 = userRepository.save(UserFixtures.모카());

			// Then
			assertThatThrownBy(() -> placeScrapService.removeScrap(모카.getId(), 스타벅스.getPosition()))
				.isInstanceOf(BusinessException.class)
				.hasMessage(ErrorCode.NOT_EXIST_SCRAP.getMessage());
		}
	}
}
