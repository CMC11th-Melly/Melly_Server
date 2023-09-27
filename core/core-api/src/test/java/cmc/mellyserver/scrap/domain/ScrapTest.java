package cmc.mellyserver.scrap.domain;

import cmc.mellyserver.mellycore.place.domain.Place;
import cmc.mellyserver.mellycore.scrap.domain.enums.ScrapType;
import cmc.mellyserver.mellycore.user.domain.User;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static cmc.mellyserver.mellycore.common.fixture.UserFixtures.모카;


public class ScrapTest {

    @DisplayName("스크랩을 생성한다")
    @Test
    void 스크랩을_생성한다() {

        // given
        User savedUser = 모카();
        Place savedPlace = Place.builder().build();

        // when
        PlaceScrap scrap = PlaceScrap.createScrap(savedUser, savedPlace, ScrapType.COMPANY);

        // then
        Assertions.assertThat(scrap.getUser()).usingRecursiveComparison().isEqualTo(savedUser);
    }
}
