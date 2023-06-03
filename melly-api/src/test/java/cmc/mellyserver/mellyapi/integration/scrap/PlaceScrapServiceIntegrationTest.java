package cmc.mellyserver.mellyapi.integration.scrap;

import static org.assertj.core.api.Assertions.*;

import java.util.Optional;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Slice;

import cmc.mellyserver.mellyapi.common.exception.GlobalBadRequestException;
import cmc.mellyserver.mellyapi.common.factory.PlaceFactory;
import cmc.mellyserver.mellyapi.common.factory.UserFactory;
import cmc.mellyserver.mellyapi.integration.IntegrationTest;
import cmc.mellyserver.mellyapi.scrap.application.dto.request.CreatePlaceScrapRequestDto;
import cmc.mellyserver.mellycore.common.enums.ScrapType;
import cmc.mellyserver.mellycore.place.domain.Place;
import cmc.mellyserver.mellycore.place.domain.Position;
import cmc.mellyserver.mellycore.scrap.domain.PlaceScrap;
import cmc.mellyserver.mellycore.scrap.domain.repository.dto.ScrapedPlaceResponseDto;
import cmc.mellyserver.mellycore.user.domain.User;

public class PlaceScrapServiceIntegrationTest extends IntegrationTest {

	@DisplayName("사용자가 스크랩한 장소를 조회한다.")
	@Test
	void find_place_login_user_scraped() {
		// given
		Place place = PlaceFactory.place();
		placeRepository.save(place);

		User user = UserFactory.createEmailLoginUser();
		User savedUser = userRepository.save(user);

		CreatePlaceScrapRequestDto createPlaceScrapRequestDto = new CreatePlaceScrapRequestDto(savedUser.getUserSeq(),
			1.234, 1.234, ScrapType.FRIEND, "스타벅스", "카페");
		placeScrapService.createScrap(createPlaceScrapRequestDto);

		// when
		Slice<ScrapedPlaceResponseDto> scrapedPlace = placeScrapService.findScrapedPlace(PageRequest.of(0, 10),
			savedUser.getUserSeq(), null);

		// then
		assertThat(scrapedPlace.getContent()).hasSize(1);
		assertThat(scrapedPlace.getContent()).extracting("placeName")
			.containsExactlyInAnyOrder("스타벅스");
	}

	@DisplayName("스크랩을 추가할때")
	@Nested
	class When_create_scrap {

		@DisplayName("스크랩하려는 장소에 DB에 없으면 스크랩과 함께 장소도 추가한다.")
		@Test
		void add_place_if_place_does_not_exist() {
			// given
			User user = UserFactory.createEmailLoginUser();
			User savedUser = userRepository.save(user);

			CreatePlaceScrapRequestDto createPlaceScrapRequestDto = new CreatePlaceScrapRequestDto(
				savedUser.getUserSeq(), 1.234, 1.234, ScrapType.FRIEND, "스타벅스", "카페");

			// when
			placeScrapService.createScrap(createPlaceScrapRequestDto);

			// then
			Optional<Place> place = placeRepository.findPlaceByPosition(new Position(1.234, 1.234));
			Optional<PlaceScrap> placeScrap = placeScrapRepository.findByUserUserSeqAndPlaceId(savedUser.getUserSeq(),
				place.get().getId());
			assertThat(place).isPresent();
			assertThat(placeScrap).isPresent();

		}

		@DisplayName("스크랩하려는 장소에 이미 스크랩을 한 상태라면 중복 예외가 발생한다.")
		@Test
		void create_scrap_with_place_create_duplicated_exception() {

			// given
			Place place = PlaceFactory.place();
			placeRepository.save(place);

			User user = UserFactory.createEmailLoginUser();
			User savedUser = userRepository.save(user);

			CreatePlaceScrapRequestDto createPlaceScrapRequestDto = new CreatePlaceScrapRequestDto(
				savedUser.getUserSeq(), 1.234, 1.234, ScrapType.FRIEND, "스타벅스", "카페");
			placeScrapService.createScrap(createPlaceScrapRequestDto);

			// then
			assertThatCode(() -> placeScrapService.createScrap(createPlaceScrapRequestDto))
				.isInstanceOf(GlobalBadRequestException.class)
				.hasMessage("중복 스크랩 할 수 없습니다.");
		}
	}

	@DisplayName("스크랩을 삭제 하려고 할때")
	@Nested
	class When_remove_scrap {

		@DisplayName("스크랩이 존재한다면 스크랩을 삭제할 수 있다.")
		@Test
		void remove_scrap() {

			// given
			Place place = PlaceFactory.place();
			Place savedPlace = placeRepository.save(place);

			User user = UserFactory.createEmailLoginUser();
			User savedUser = userRepository.save(user);

			CreatePlaceScrapRequestDto createPlaceScrapRequestDto = new CreatePlaceScrapRequestDto(
				savedUser.getUserSeq(), 1.234, 1.234, ScrapType.FRIEND, "스타벅스", "카페");
			placeScrapService.createScrap(createPlaceScrapRequestDto);

			// when
			placeScrapService.removeScrap(savedUser.getUserSeq(), 1.234, 1.234);

			// then
			Optional<PlaceScrap> result = placeScrapRepository.findByUserUserSeqAndPlaceId(savedUser.getUserSeq(),
				savedPlace.getId());
			assertThat(result).isEmpty();

		}

		@DisplayName("기존에 스크랩이 존재하지 않는다면 삭제하지 못하고 예외가 발생한다.")
		@Test
		void remove_scrap_invalid() {

			// given
			Place place = PlaceFactory.place();
			placeRepository.save(place);

			User user = UserFactory.createEmailLoginUser();
			User savedUser = userRepository.save(user);

			// then
			assertThatCode(() -> placeScrapService.removeScrap(savedUser.getUserSeq(), 1.234, 1.234))
				.isInstanceOf(GlobalBadRequestException.class)
				.hasMessage("스크랩 취소할 수 없습니다.");
		}

		@DisplayName("삭제할 스크랩이 할당된 장소가 존재하지 않으면 예외가 발생한다.")
		@Test
		void remove_place_scrap_belong_not_found_exception() {

			// given
			User user = UserFactory.createEmailLoginUser();
			User savedUser = userRepository.save(user);

			// then
			assertThatCode(() -> placeScrapService.removeScrap(savedUser.getUserSeq(), 1.234, 1.234))
				.isInstanceOf(GlobalBadRequestException.class)
				.hasMessage("해당하는 장소가 없습니다.");
		}
	}
}
