package cmc.mellyserver.domain.place.integration;

import static fixtures.PlaceFixtures.*;
import static fixtures.UserFixtures.*;
import static org.assertj.core.api.Assertions.*;

import java.util.List;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import cmc.mellyserver.dbcore.memory.MemoryRepository;
import cmc.mellyserver.dbcore.memory.OpenType;
import cmc.mellyserver.dbcore.place.Place;
import cmc.mellyserver.dbcore.place.PlaceRepository;
import cmc.mellyserver.dbcore.place.Position;
import cmc.mellyserver.dbcore.scrap.PlaceScrap;
import cmc.mellyserver.dbcore.scrap.PlaceScrapRepository;
import cmc.mellyserver.dbcore.scrap.ScrapType;
import cmc.mellyserver.dbcore.user.User;
import cmc.mellyserver.dbcore.user.UserRepository;
import cmc.mellyserver.domain.memory.query.dto.FindPlaceByMemoryTitleResponseDto;
import cmc.mellyserver.domain.place.PlaceService;
import cmc.mellyserver.domain.scrap.dto.MarkedPlaceResponseDto;
import cmc.mellyserver.domain.scrap.dto.PlaceResponseDto;
import cmc.mellyserver.support.IntegrationTestSupport;
import cmc.mellyserver.support.exception.BusinessException;
import cmc.mellyserver.support.exception.ErrorCode;
import fixtures.MemoryFixtures;

public class PlaceServiceTest extends IntegrationTestSupport {

	@Autowired
	private PlaceService placeService;

	@Autowired
	private PlaceRepository placeRepository;

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private MemoryRepository memoryRepository;

	@Autowired
	private PlaceScrapRepository placeScrapRepository;

	@DisplayName("사용자의 메모리가 하나 이상 존재하는 장소들을 조회한다")
	@Test
	void 사용자의_메모리가_하나이상_존재하는_장소들을_조회한다() {

		// given
		User 모카 = userRepository.save(모카());
		User 머식 = userRepository.save(머식());

		Place 스타벅스 = placeRepository.save(스타벅스());
		Place 이디야 = placeRepository.save(이디야());

		memoryRepository.save(MemoryFixtures.메모리(스타벅스.getId(), 모카.getId(), null, "스타벅스 방문!", OpenType.ALL));
		memoryRepository.save(MemoryFixtures.메모리(이디야.getId(), 머식.getId(), null, "이디야 방문!", OpenType.ALL));

		// when
		List<MarkedPlaceResponseDto> result = placeService.displayMarkedPlace(모카.getId(), null);

		// then
		assertThat(result).hasSize(1);
	}

	@DisplayName("메모리 제목으로 장소를 조회한다")
	@Test
	void 메모리_제목으로_장소를_조회한다() {

		// given
		User 모카 = userRepository.save(모카());
		Place 스타벅스 = placeRepository.save(스타벅스());

		memoryRepository.save(MemoryFixtures.메모리(스타벅스.getId(), 모카.getId(), null, "스타벅스 방문!", OpenType.ALL));

		// when
		List<FindPlaceByMemoryTitleResponseDto> places = placeService.findByMemoryTitle(모카.getId(), "스타벅");

		// then
		Assertions.assertThat(places).hasSize(1);
	}

	@DisplayName("장소 ID로 조회했을때")
	@Nested
	class 장소_ID로_조회했을때 {

		@DisplayName("장소 정보를 조회한다")
		@Test
		void 장소_정보를_조회한다() {

			// given
			Place 스타벅스 = placeRepository.save(스타벅스());
			User 모카 = userRepository.save(모카());

			// when
			PlaceResponseDto result = placeService.findByPlaceId(모카.getId(), 스타벅스.getId());

			// then
			assertThat(result.getPlaceId()).isEqualTo(스타벅스.getId());
			assertThat(result.getIsScraped()).isFalse();
		}

		@DisplayName("사용자가 장소를 스크랩한 경우 스크랩 여부를 true로 반환한다")
		@Test
		void 사용자가_장소를_스크랩한_경우_스크랩_여부를_true로_반환한다() {

			// given
			Place 스타벅스 = placeRepository.save(스타벅스());
			User 모카 = userRepository.save(모카());
			placeScrapRepository.save(PlaceScrap.createScrap(모카, 스타벅스, ScrapType.FRIEND));

			// when
			PlaceResponseDto result = placeService.findByPlaceId(모카.getId(), 스타벅스.getId());

			// then
			assertThat(result.getPlaceId()).isEqualTo(스타벅스.getId());
			assertThat(result.getIsScraped()).isTrue();
		}

		@DisplayName("장소가 없다면 예외가 발생한다")
		@Test
		void 장소ID로_장소_정보를_조회할수_없으면_예외를_반환한다() {

			// given
			User 모카 = userRepository.save(모카());

			// when & then
			assertThatThrownBy(() ->
				placeService.findByPlaceId(모카.getId(), -1L)
			).isInstanceOf(BusinessException.class).hasMessage(ErrorCode.NO_SUCH_PLACE.getMessage());
		}

	}

	@DisplayName("장소 좌표로 조회했을때")
	@Nested
	class 장소_좌표로_조회했을때 {

		@DisplayName("장소 정보를 조회한다")
		@Test
		void 장소_좌표로_장소_정보를_조회한다() {

			// given
			Place 스타벅스 = placeRepository.save(스타벅스());
			User 모카 = userRepository.save(모카());

			// when
			PlaceResponseDto result = placeService.findByPosition(모카.getId(), 스타벅스.getPosition());

			// then
			assertThat(result.getPlaceId()).isEqualTo(스타벅스.getId());
			assertThat(result.getIsScraped()).isFalse();
		}

		@DisplayName("사용자가 장소를 스크랩한 경우 좌표 조회 시 스크랩 여부를 true로 반환한다")
		@Test
		void 사용자가_장소를_스크랩한_경우_좌표_조회시_스크랩_여부를_true로_반환한다() {

			// given
			Place 스타벅스 = placeRepository.save(스타벅스());
			User 모카 = userRepository.save(모카());
			placeScrapRepository.save(PlaceScrap.createScrap(모카, 스타벅스, ScrapType.FRIEND));

			// when
			PlaceResponseDto result = placeService.findByPosition(모카.getId(), 스타벅스.getPosition());

			// then
			assertThat(result.getPlaceId()).isEqualTo(스타벅스.getId());
			assertThat(result.getIsScraped()).isTrue();
		}

		@DisplayName("장소가 없다면 예외가 발생한다")
		@Test
		void 장소_좌표로_장소_정보를_조회할수_없으면_예외를_반환한다() {

			// given
			User 모카 = userRepository.save(모카());

			// when & then
			assertThatThrownBy(() ->
				placeService.findByPosition(모카.getId(), new Position(1.000, 1.000))
			).isInstanceOf(BusinessException.class).hasMessage(ErrorCode.NO_SUCH_PLACE.getMessage());
		}
	}

}
