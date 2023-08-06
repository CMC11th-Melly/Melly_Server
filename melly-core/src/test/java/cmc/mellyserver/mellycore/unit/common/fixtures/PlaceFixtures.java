package cmc.mellyserver.mellycore.unit.common.fixtures;

import cmc.mellyserver.mellycore.place.domain.Place;
import cmc.mellyserver.mellycore.place.domain.Position;

public class PlaceFixtures {

    public static Place 테스트_장소_1() {
        return Place.builder()
                .position(new Position(1.234, 1.234))
                .placeName("스타벅스")
                .placeImage("place_image_link.png")
                .placeCategory("카페/베이커리")
                .isDeleted(Boolean.FALSE)
                .build();
    }
}
