package cmc.mellyserver.place.domain.service;

import cmc.mellyserver.auth.presentation.dto.Provider;
import cmc.mellyserver.common.util.auth.AuthenticatedUserChecker;
import cmc.mellyserver.place.application.dto.PlaceResponseDto;
import cmc.mellyserver.place.domain.Place;
import cmc.mellyserver.place.domain.PlaceRepository;
import cmc.mellyserver.place.domain.Position;
import cmc.mellyserver.user.domain.User;
import cmc.mellyserver.user.domain.UserRepository;
import cmc.mellyserver.user.domain.enums.RoleType;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.BDDMockito;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

@ExtendWith(MockitoExtension.class)
class PlaceDomainServiceTest {

    @InjectMocks
    PlaceDomainService placeDomainService;

    @Mock
    PlaceRepository placeRepository;

    @Mock
    UserRepository userRepository;

    @Mock
    AuthenticatedUserChecker authenticatedUserChecker;

    @Test
    @DisplayName("특정 장소의 좌표를 받으면 그 장소의 스크랩 여부와 메모리 개수 보여줌")
    public void show_place_info()
    {
        // given
        BDDMockito.given(placeRepository.findPlaceByPosition(BDDMockito.any()))
                .willReturn(Optional.of(new Place(new Position(37.0001,127.0001), null,"카페, 디저트","한양대학교")));
        BDDMockito.given(authenticatedUserChecker.checkAuthenticatedUserExist(BDDMockito.anyString())).willReturn(new User("1", Provider.DEFAULT, RoleType.USER,"asdfasdf"));

        // when
        PlaceResponseDto place = placeDomainService.getPlace("1", 37.0001, 127.0001);

        // then
        Assertions.assertThat(place.getPlaceId()).isNull();
        Assertions.assertThat(place.getPlaceImage()).isNull();
        Assertions.assertThat(place.getMyMemoryCount()).isEqualTo(0);
        Assertions.assertThat(place.getPlaceName()).isEqualTo("한양대학교");
        Assertions.assertThat(place.getIsScraped()).isFalse();
        Assertions.assertThat(place.getPlaceCategory()).isEqualTo("카페, 디저트");
        Assertions.assertThat(place.getPosition()).isEqualTo(new Position(37.0001,127.0001));
    }

    @Test
    @DisplayName("좌표 조회했을때 장소 데이터가 없다면 placeId 값은 -1이 나온다.")
    public void show_place_info_when_place_not_exist()
    {
        // given
        BDDMockito.given(placeRepository.findPlaceByPosition(BDDMockito.any()))
                .willReturn(Optional.empty());

        // when
        PlaceResponseDto place = placeDomainService.getPlace("1", 37.0001, 127.0001);

        // then 
        Assertions.assertThat(place.getPlaceId()).isEqualTo(-1L);

    }

}