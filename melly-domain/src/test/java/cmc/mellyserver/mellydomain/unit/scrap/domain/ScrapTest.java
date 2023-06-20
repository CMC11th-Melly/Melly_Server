package cmc.mellyserver.mellydomain.unit.scrap.domain;

import cmc.mellyserver.mellydomain.common.enums.ScrapType;
import cmc.mellyserver.mellydomain.place.domain.Place;
import cmc.mellyserver.mellydomain.place.domain.Position;
import cmc.mellyserver.mellydomain.scrap.domain.PlaceScrap;
import cmc.mellyserver.mellydomain.user.domain.User;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class ScrapTest {

    @DisplayName("스크랩을 생성한다.")
    @Test
    void create_scrap() {
        // given
        User savedUser = User.builder().nickname("테스트 유저").build();
        Place savedPlace = Place.builder().placeName("테스트 장소").position(new Position(1.234, 1.234))
                .build();

        // when
        PlaceScrap scrap = PlaceScrap.createScrap(savedUser, savedPlace, ScrapType.COMPANY);

        // then
        Assertions.assertThat(scrap.getUser()).usingRecursiveComparison()
                .isEqualTo(savedUser);
    }
}
