package cmc.mellyserver.unit.scrap.application;

import cmc.mellyserver.common.enums.GroupType;
import cmc.mellyserver.common.enums.ScrapType;
import cmc.mellyserver.common.factory.UserFactory;
import cmc.mellyserver.common.util.auth.AuthenticatedUserChecker;
import cmc.mellyserver.place.domain.Position;
import cmc.mellyserver.place.domain.repository.PlaceQueryRepository;
import cmc.mellyserver.place.domain.repository.PlaceRepository;
import cmc.mellyserver.scrap.application.dto.response.PlaceScrapCountResponseDto;
import cmc.mellyserver.scrap.application.dto.response.ScrapedPlaceResponseDto;
import cmc.mellyserver.scrap.application.impl.PlaceScrapServiceImpl;
import cmc.mellyserver.scrap.domain.repository.PlaceScrapRepository;
import cmc.mellyserver.user.domain.User;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.SliceImpl;
import java.util.List;
import static org.assertj.core.api.Assertions.*;
import static org.mockito.BDDMockito.*;


@ExtendWith(MockitoExtension.class)
public class PlaceScrapServiceTest {

    @InjectMocks
    private PlaceScrapServiceImpl placeScrapService;

    @Mock
    private PlaceRepository placeRepository;

    @Mock
    PlaceQueryRepository placeQueryRepository;

    @Mock
    private PlaceScrapRepository scrapRepository;

    @Mock
    private AuthenticatedUserChecker authenticatedUserChecker;

    @DisplayName("사용자가 스크랩한 장소를 조회한다.")
    @Test
    void find_place_login_user_scraped(){

        // given
        List<ScrapedPlaceResponseDto> scrapedPlaceResponseDtos = List.of(
                new ScrapedPlaceResponseDto(1L,new Position(1.1234,1.1234),10L,20L,true,"카페","투썸플레이스", GroupType.FRIEND,"testImage.png"),
                new ScrapedPlaceResponseDto(2L,new Position(1.1234,1.1234),10L,20L,true,"카페","스타벅스",GroupType.FRIEND,"testImage.png")
        );

        Slice<ScrapedPlaceResponseDto> scrapedPlaceResponseDtoSlice = new SliceImpl<>(scrapedPlaceResponseDtos, PageRequest.of(0, 2), false);

        given(placeQueryRepository.getScrapedPlace(any(Pageable.class),anyLong(),any(ScrapType.class)))
                .willReturn(scrapedPlaceResponseDtoSlice);
        // when
        Slice<ScrapedPlaceResponseDto> scrapedPlace = placeScrapService.findScrapedPlace(PageRequest.of(0,2), 1L, ScrapType.FRIEND);

        // then
        assertThat(scrapedPlace).usingRecursiveComparison()
                .isEqualTo(scrapedPlaceResponseDtoSlice);

        verify(placeQueryRepository,times(1))
                .getScrapedPlace(any(Pageable.class),anyLong(),any(ScrapType.class));
    }


    @DisplayName("사용자가 스크랩한 장소들의 숫자를 스크랩 장소별로 보여준다.")
    @Test
    void find_place_login_user_scraped_count(){

        // given
        List<PlaceScrapCountResponseDto> placeScrapCountResponseDtos = List.of(
                new PlaceScrapCountResponseDto(ScrapType.FRIEND,2L),
                new PlaceScrapCountResponseDto(ScrapType.COMPANY,3L)
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

        verify(authenticatedUserChecker,times(1)).checkAuthenticatedUserExist(anyLong());

        verify(placeQueryRepository,times(1)).getScrapedPlaceGrouping(any(User.class));
    }

    @DisplayName("스크랩을 추가할 수 있다.")
    @Test
    void create_scrap(){

        // given

        // when

        // then

    }

    @DisplayName("스크랩을 삭제할 수 있다.")
    @Test
    void remove_scrap(){

        // given

        // when

        // then

    }

}
