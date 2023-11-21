package cmc.mellyserver.domain.scrap.dto;

import java.util.HashMap;

import cmc.mellyserver.dbcore.place.Place;
import cmc.mellyserver.dbcore.place.Position;
import lombok.Builder;

public record PlaceResponseDto(

    Long placeId,
    Position position,
    long myMemoryCount,

    long otherMemoryCount,

    boolean isScraped,

    String placeCategory,

    String placeName,

    String placeImage
) {

    private static final String MY_MEMORY = "myMemory";
    private static final String OTHER_MEMORY = "otherMemory";

    @Builder
    public PlaceResponseDto {
    }

    public static PlaceResponseDto of(Place place, boolean isUserScraped, HashMap<String, Long> memoryCount) {
        return new PlaceResponseDto(place.getId(), place.getPosition(), memoryCount.get(MY_MEMORY),
            memoryCount.get(OTHER_MEMORY), isUserScraped, place.getCategory(), place.getName(),
            place.getImageUrl());
    }

}
