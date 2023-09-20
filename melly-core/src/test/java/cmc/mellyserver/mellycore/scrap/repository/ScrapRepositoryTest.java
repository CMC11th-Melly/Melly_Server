package cmc.mellyserver.mellycore.scrap.repository;

import cmc.mellyserver.mellycore.common.fixture.PlaceFixtures;
import cmc.mellyserver.mellycore.common.fixture.UserFixtures;
import cmc.mellyserver.mellycore.config.RepositoryTest;
import cmc.mellyserver.mellycore.place.domain.Place;
import cmc.mellyserver.mellycore.place.domain.repository.PlaceRepository;
import cmc.mellyserver.mellycore.scrap.domain.PlaceScrap;
import cmc.mellyserver.mellycore.scrap.domain.enums.ScrapType;
import cmc.mellyserver.mellycore.scrap.domain.repository.PlaceScrapRepository;
import cmc.mellyserver.mellycore.user.domain.User;
import cmc.mellyserver.mellycore.user.domain.repository.UserRepository;
import org.assertj.core.api.Assertions;
import org.junit.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import java.util.Optional;


public class ScrapRepositoryTest extends RepositoryTest {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PlaceRepository placeRepository;

    @Autowired
    private PlaceScrapRepository placeScrapRepository;

    @Autowired
    private TestEntityManager entityManager;

    User user;

    Place place;

    @BeforeEach
    void setUp() {

        user = userRepository.save(UserFixtures.모카());
        place = placeRepository.save(PlaceFixtures.스타벅스());

        placeScrapRepository.save(PlaceScrap.createScrap(user, place, ScrapType.FRIEND));

        entityManager.flush();
        entityManager.clear();
    }

    @DisplayName("스크랩을 한 유저의 ID와 장소 ID로 해당 스크랩을 찾을 수 있다")
    @Test
    public void 스크랩을한_유저의_ID와_장소ID로_스크랩을_찾을수_있다() {

        // when
        Optional<PlaceScrap> result = placeScrapRepository.findByUserIdAndPlaceId(user.getId(), place.getId());

        // then
        Assertions.assertThat(result.get().getUser().getNickname()).isEqualTo(user.getNickname());
        Assertions.assertThat(result.get().getPlace().getPlaceName()).isEqualTo(place.getPlaceName());
    }
}
