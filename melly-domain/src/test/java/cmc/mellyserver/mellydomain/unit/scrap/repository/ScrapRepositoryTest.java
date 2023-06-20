package cmc.mellyserver.mellydomain.unit.scrap.repository;

import cmc.mellyserver.mellydomain.common.enums.ScrapType;
import cmc.mellyserver.mellydomain.place.domain.Place;
import cmc.mellyserver.mellydomain.place.domain.Position;
import cmc.mellyserver.mellydomain.place.domain.repository.PlaceRepository;
import cmc.mellyserver.mellydomain.scrap.domain.PlaceScrap;
import cmc.mellyserver.mellydomain.scrap.domain.repository.PlaceScrapRepository;
import cmc.mellyserver.mellydomain.unit.RepositoryTest;
import cmc.mellyserver.mellydomain.unit.common.fixtures.UserFixtures;
import cmc.mellyserver.mellydomain.user.domain.User;
import cmc.mellyserver.mellydomain.user.domain.repository.UserRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import java.util.List;


public class ScrapRepositoryTest extends RepositoryTest {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PlaceRepository placeRepository;

    @Autowired
    private PlaceScrapRepository placeScrapRepository;

    @Autowired
    private TestEntityManager testEntityManager;

    private List<User> users;

    private Place place;

    @BeforeEach
    void setUp() {

        users = UserFixtures.mockLikeUsersWithId();
        users.forEach(user -> userRepository.save(user));

        place = placeRepository.save(
                Place.builder().placeName("테스트 장소").position(new Position(1.234, 1.234)).build());

        testEntityManager.flush();
        testEntityManager.clear();
    }


    @DisplayName("스크랩을 한 유저의 식별자와 장소 ID로 해당 스크랩을 찾을 수 있다.")
    @Test
    void find_scrap_by_userseq_and_placeId() {

        // when
        PlaceScrap scrap = placeScrapRepository.save(
                PlaceScrap.createScrap(users.get(0), place, ScrapType.FRIEND));

        // then
        Assertions.assertThat(scrap.getUser().getNickname()).isEqualTo(users.get(0).getNickname());
        Assertions.assertThat(scrap.getPlace().getPlaceName()).isEqualTo("테스트 장소");
    }


}
