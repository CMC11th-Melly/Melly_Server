package cmc.mellyserver.common.factory;

import cmc.mellyserver.place.domain.Place;
import cmc.mellyserver.place.domain.Position;

public class PlaceFactory {

    public static Place mockPlace()
    {
        return Place.builder()
                .id(1L)
                .position(new Position(1.234,1.234))
                .placeCategory("카페")
                .placeName("스타벅스")
                .placeImage("testImage")
                .build();
    }

    public static Place place()
    {
        return Place.builder()
                .position(new Position(1.234,1.234))
                .placeCategory("카페")
                .placeName("스타벅스")
                .placeImage("testImage")
                .build();
    }

}
