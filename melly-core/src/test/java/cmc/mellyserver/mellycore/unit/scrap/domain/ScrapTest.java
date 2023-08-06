package cmc.mellyserver.mellycore.unit.scrap.domain;

import cmc.mellyserver.mellycommon.enums.ScrapType;
import cmc.mellyserver.mellycore.place.domain.Place;
import cmc.mellyserver.mellycore.scrap.domain.PlaceScrap;
import cmc.mellyserver.mellycore.unit.common.fixtures.PlaceFixtures;
import cmc.mellyserver.mellycore.unit.common.fixtures.UserFixtures;
import cmc.mellyserver.mellycore.user.domain.User;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class ScrapTest {

    @DisplayName("스크랩을 생성한다")
    @Test
    void 스크랩을_생성한다() {
        // given
        User savedUser = UserFixtures.테스트_유저_1();
        Place savedPlace = PlaceFixtures.테스트_장소_1();

        // when
        PlaceScrap scrap = PlaceScrap.createScrap(savedUser, savedPlace, ScrapType.COMPANY);

        // then
        Assertions.assertThat(scrap.getUser()).usingRecursiveComparison()
                .isEqualTo(savedUser);
    }
}
