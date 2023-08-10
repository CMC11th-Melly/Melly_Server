package cmc.mellyserver.mellycore.unit.scrap.repository;

import cmc.mellyserver.mellycore.place.domain.Place;
import cmc.mellyserver.mellycore.place.domain.Position;
import cmc.mellyserver.mellycore.place.domain.repository.PlaceRepository;
import cmc.mellyserver.mellycore.scrap.domain.PlaceScrap;
import cmc.mellyserver.mellycore.scrap.domain.repository.PlaceScrapQueryRepository;
import cmc.mellyserver.mellycore.scrap.domain.repository.PlaceScrapRepository;
import cmc.mellyserver.mellycore.scrap.domain.enums.ScrapType;
import cmc.mellyserver.mellycore.unit.RepositoryTest;
import cmc.mellyserver.mellycore.user.domain.User;
import cmc.mellyserver.mellycore.user.domain.repository.UserRepository;
import cmc.mellyserver.mellycore.user.domain.enums.Provider;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

public class ScrapQueryRepositoryTest extends RepositoryTest {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PlaceRepository placeRepository;

    @Autowired
    private PlaceScrapRepository placeScrapRepository;

    @Autowired
    private PlaceScrapQueryRepository placeScrapQueryRepository;


    @DisplayName("로그인한 유저가 현재 장소를 스크랩했는지를 장소 좌표 기준으로 체크한다.")
    @Test
    void check_login_user_scraped_by_position() {

        // given
        User user = User.builder().nickname("테스트 유저").provider(Provider.DEFAULT).build();
        User savedUser = userRepository.save(user);

        Place place = Place.builder().position(new Position(1.234, 1.234)).placeName("테스트 장소")
                .build();
        Place savedPlace = placeRepository.save(place);

        PlaceScrap scrap = PlaceScrap.createScrap(savedUser, savedPlace, ScrapType.FRIEND);
        placeScrapRepository.save(scrap);

        // when
        Boolean isLoginUserScrapedPlace = placeScrapQueryRepository.checkCurrentLoginUserScrapedPlaceByPosition(
                savedUser.getId(), savedPlace.getPosition());

        // then
        Assertions.assertThat(isLoginUserScrapedPlace).isTrue();
    }

    @DisplayName("로그인한 유저가 현재 장소를 스크랩했는지를 장소 식별자를 기준으로 체크한다.")
    @Test
    void check_login_user_scraped_by_place_id() {

        // given
        User user = User.builder().nickname("테스트 유저").provider(Provider.DEFAULT).build();
        User savedUser = userRepository.save(user);

        Place place = Place.builder().position(new Position(1.234, 1.234)).placeName("테스트 장소")
                .build();
        Place savedPlace = placeRepository.save(place);

        PlaceScrap scrap = PlaceScrap.createScrap(savedUser, savedPlace, ScrapType.FRIEND);
        placeScrapRepository.save(scrap);

        // when
        Boolean isLoginUserScrapedPlace = placeScrapQueryRepository.checkCurrentLoginUserScrapedPlaceByPlaceId(
                savedUser.getId(), savedPlace.getId());

        // then
        Assertions.assertThat(isLoginUserScrapedPlace).isTrue();
    }
}


