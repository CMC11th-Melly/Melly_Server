package cmc.mellyserver.mellyapi.unit.scrap.application;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.BDDMockito.*;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.SliceImpl;

import cmc.mellyserver.mellyapi.common.auth.AuthenticatedUserChecker;
import cmc.mellyserver.mellyapi.common.exception.GlobalBadRequestException;
import cmc.mellyserver.mellyapi.common.factory.PlaceFactory;
import cmc.mellyserver.mellyapi.common.factory.PlaceScrapFactory;
import cmc.mellyserver.mellyapi.common.factory.UserFactory;
import cmc.mellyserver.mellyapi.scrap.application.dto.request.CreatePlaceScrapRequestDto;
import cmc.mellyserver.mellyapi.scrap.application.impl.PlaceScrapServiceImpl;
import cmc.mellyserver.mellycore.common.enums.GroupType;
import cmc.mellyserver.mellycore.common.enums.ScrapType;
import cmc.mellyserver.mellycore.place.domain.Place;
import cmc.mellyserver.mellycore.place.domain.Position;
import cmc.mellyserver.mellycore.place.domain.repository.PlaceQueryRepository;
import cmc.mellyserver.mellycore.place.domain.repository.PlaceRepository;
import cmc.mellyserver.mellycore.scrap.domain.PlaceScrap;
import cmc.mellyserver.mellycore.scrap.domain.repository.PlaceScrapRepository;
import cmc.mellyserver.mellycore.scrap.domain.repository.dto.PlaceScrapCountResponseDto;
import cmc.mellyserver.mellycore.scrap.domain.repository.dto.ScrapedPlaceResponseDto;
import cmc.mellyserver.mellycore.user.domain.User;

@ExtendWith(MockitoExtension.class)
public class PlaceScrapServiceTest {

	@InjectMocks
	private PlaceScrapServiceImpl placeScrapService;

	@Mock
	private PlaceRepository placeRepository;

	@Mock
	private PlaceQueryRepository placeQueryRepository;

	@Mock
	private PlaceScrapRepository scrapRepository;

	@Mock
	private AuthenticatedUserChecker authenticatedUserChecker;

	@DisplayName("사용자가 스크랩한 장소를 조회한다.")
	@Test
	void find_place_login_user_scraped() {

		// given
		List<ScrapedPlaceResponseDto> scrapedPlaceResponseDtos = List.of(
			new ScrapedPlaceResponseDto(1L, new Position(1.1234, 1.1234), 10L, 20L, true, "카페", "투썸플레이스",
				GroupType.FRIEND, "testImage.png"),
			new ScrapedPlaceResponseDto(2L, new Position(1.1234, 1.1234), 10L, 20L, true, "카페", "스타벅스",
				GroupType.FRIEND, "testImage.png")
		);

		Slice<ScrapedPlaceResponseDto> scrapedPlaceResponseDtoSlice = new SliceImpl<>(scrapedPlaceResponseDtos,
			PageRequest.of(0, 2), false);

		given(placeQueryRepository.getScrapedPlace(any(Pageable.class), anyLong(), any(ScrapType.class)))
			.willReturn(scrapedPlaceResponseDtoSlice);
		// when
		Slice<ScrapedPlaceResponseDto> scrapedPlace = placeScrapService.findScrapedPlace(PageRequest.of(0, 2), 1L,
			ScrapType.FRIEND);

		// then
		assertThat(scrapedPlace).usingRecursiveComparison()
			.isEqualTo(scrapedPlaceResponseDtoSlice);

		verify(placeQueryRepository, times(1))
			.getScrapedPlace(any(Pageable.class), anyLong(), any(ScrapType.class));
	}

	@DisplayName("사용자가 스크랩한 장소들의 숫자를 스크랩 장소별로 보여준다.")
	@Test
	void find_place_login_user_scraped_count() {

		// given
		List<PlaceScrapCountResponseDto> placeScrapCountResponseDtos = List.of(
			new PlaceScrapCountResponseDto(ScrapType.FRIEND, 2L),
			new PlaceScrapCountResponseDto(ScrapType.COMPANY, 3L)
		);

		User user = UserFactory.createEmailLoginUser();

		given(authenticatedUserChecker.checkAuthenticatedUserExist(anyLong())).willReturn(user);

		given(placeQueryRepository.getScrapedPlaceGrouping(any(User.class)))
			.willReturn(placeScrapCountResponseDtos);

		// when
		List<PlaceScrapCountResponseDto> placeScrapCountResponseDtos1 = placeScrapService.countByPlaceScrapType(1L);

		// then
		assertThat(placeScrapCountResponseDtos1)
			.usingRecursiveComparison().isEqualTo(placeScrapCountResponseDtos);

		verify(authenticatedUserChecker, times(1)).checkAuthenticatedUserExist(anyLong());

		verify(placeQueryRepository, times(1)).getScrapedPlaceGrouping(any(User.class));
	}

	@DisplayName("스크랩을 추가할때")
	@Nested
	class When_create_scrap {

		@DisplayName("스크랩하려는 장소 정보가 DB에 없으면 장소를 추가한다.")
		@Test
		void create_scrap_with_place_create() {

			// given
			CreatePlaceScrapRequestDto createPlaceScrapRequestDto = PlaceScrapFactory.mockCreatePlaceScrapRequestDto();

			given(placeRepository.findPlaceByPosition(any(Position.class)))
				.willReturn(Optional.empty());

			given(authenticatedUserChecker.checkAuthenticatedUserExist(anyLong()))
				.willReturn(UserFactory.createEmailLoginUser());

			given(placeRepository.save(any(Place.class)))
				.willReturn(PlaceFactory.mockPlace());

			given(scrapRepository.save(any(PlaceScrap.class))).willReturn(null);

			// when
			placeScrapService.createScrap(createPlaceScrapRequestDto);

			// then
			verify(placeRepository, times(1)).findPlaceByPosition(any(Position.class));
			verify(authenticatedUserChecker, times(1)).checkAuthenticatedUserExist(anyLong());
			verify(placeRepository, times(1)).save(any(Place.class));
		}

		@DisplayName("스크랩하려는 장소가 DB에 존재하면 스크랩만 추가한다.")
		@Test
		void create_scrap_without_place_create() {

			// given
			CreatePlaceScrapRequestDto createPlaceScrapRequestDto = PlaceScrapFactory.mockCreatePlaceScrapRequestDto();

			given(placeRepository.findPlaceByPosition(any(Position.class)))
				.willReturn(Optional.of(PlaceFactory.mockPlace()));

			given(authenticatedUserChecker.checkAuthenticatedUserExist(anyLong()))
				.willReturn(UserFactory.createEmailLoginUser());

			given(scrapRepository.findByUserUserSeqAndPlaceId(anyLong(), anyLong()))
				.willReturn(Optional.empty());

			given(scrapRepository.save(any(PlaceScrap.class))).willReturn(null);

			// when
			placeScrapService.createScrap(createPlaceScrapRequestDto);

			// then
			verify(placeRepository, times(1)).findPlaceByPosition(any(Position.class));
			verify(authenticatedUserChecker, times(1)).checkAuthenticatedUserExist(anyLong());
			verify(scrapRepository, times(1)).findByUserUserSeqAndPlaceId(anyLong(), anyLong());
		}

		@DisplayName("스크랩하려는 장소에 이미 스크랩을 한 상태라면 중복 예외가 발생한다.")
		@Test
		void create_scrap_with_place_create_duplicated_exception() {

			// given
			CreatePlaceScrapRequestDto createPlaceScrapRequestDto = PlaceScrapFactory.mockCreatePlaceScrapRequestDto();

			given(placeRepository.findPlaceByPosition(any(Position.class)))
				.willReturn(Optional.of(PlaceFactory.mockPlace()));

			given(authenticatedUserChecker.checkAuthenticatedUserExist(anyLong()))
				.willReturn(UserFactory.createEmailLoginUser());

			given(scrapRepository.findByUserUserSeqAndPlaceId(anyLong(), anyLong()))
				.willReturn(Optional.of(PlaceScrapFactory.mockPlaceScrap()));

			// then
			assertThatCode(() -> placeScrapService.createScrap(createPlaceScrapRequestDto))
				.isInstanceOf(GlobalBadRequestException.class)
				.hasMessage("중복 스크랩 할 수 없습니다.");

			verify(placeRepository, times(1)).findPlaceByPosition(any(Position.class));
			verify(authenticatedUserChecker, times(1)).checkAuthenticatedUserExist(anyLong());
			verify(scrapRepository, times(1)).findByUserUserSeqAndPlaceId(anyLong(), anyLong());

		}
	}

	@DisplayName("스크랩을 삭제 하려고 할때")
	@Nested
	class When_remove_scrap {

		@DisplayName("스크랩이 존재한다면 스크랩을 삭제할 수 있다.")
		@Test
		void remove_scrap() {

			// given
			given(placeRepository.findPlaceByPosition(any(Position.class)))
				.willReturn(Optional.of(PlaceFactory.mockPlace()));

			given(scrapRepository.findByUserUserSeqAndPlaceId(anyLong(), anyLong()))
				.willReturn(Optional.of(PlaceScrapFactory.mockPlaceScrap()));

			// when
			placeScrapService.removeScrap(1L, 1.234, 1.234);

			// then
			verify(placeRepository, times(1)).findPlaceByPosition(any(Position.class));
			verify(scrapRepository, times(1)).findByUserUserSeqAndPlaceId(anyLong(), anyLong());
			verify(scrapRepository, times(1)).deleteByUserUserSeqAndPlacePosition(anyLong(), any(Position.class));
		}

		@DisplayName("기존에 스크랩이 존재하지 않는다면 삭제하지 못하고 예외가 발생한다.")
		@Test
		void remove_scrap_invalid() {

			// given
			given(placeRepository.findPlaceByPosition(any(Position.class)))
				.willReturn(Optional.of(PlaceFactory.mockPlace()));

			given(scrapRepository.findByUserUserSeqAndPlaceId(anyLong(), anyLong()))
				.willReturn(Optional.empty());

			// then
			assertThatCode(() -> placeScrapService.removeScrap(1L, 1.234, 1.234))
				.isInstanceOf(GlobalBadRequestException.class)
				.hasMessage("스크랩 취소할 수 없습니다.");

			verify(placeRepository, times(1)).findPlaceByPosition(any(Position.class));
			verify(scrapRepository, times(1)).findByUserUserSeqAndPlaceId(anyLong(), anyLong());
		}

		@DisplayName("삭제할 스크랩이 할당된 장소가 존재하지 않으면 예외가 발생한다.")
		@Test
		void remove_place_scrap_belong_not_found_exception() {

			// given
			given(placeRepository.findPlaceByPosition(any(Position.class)))
				.willReturn(Optional.empty());

			// then
			assertThatCode(() -> placeScrapService.removeScrap(1L, 1.234, 1.234))
				.isInstanceOf(GlobalBadRequestException.class)
				.hasMessage("해당하는 장소가 없습니다.");

			verify(placeRepository, times(1)).findPlaceByPosition(any(Position.class));
		}
	}
}
