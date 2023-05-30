package cmc.mellyserver.common.factory;

import cmc.mellyserver.common.enums.GroupType;
import cmc.mellyserver.place.application.dto.MarkedPlaceResponseDto;
import cmc.mellyserver.place.application.dto.PlaceResponseDto;
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

    public static MarkedPlaceResponseDto mockMarkedPlaceResponseDto()
    {
        return MarkedPlaceResponseDto.builder()
                .placeId(1L)
                .groupType(GroupType.FRIEND)
                .memoryCount(5L)
                .position(new Position(1.234,1.234))
                .build();
    }

    public static PlaceResponseDto mockPlaceResponseDto()
    {
        return PlaceResponseDto.builder()
                .myMemoryCount(5L)
                .otherMemoryCount(5L)
                .position(new Position(1.234,1.234))
                .isScraped(true)
                .build();

    }

}
