package cmc.mellyserver.controller.place.dto;

import cmc.mellyserver.controller.place.dto.response.PlaceResponse;
import cmc.mellyserver.domain.scrap.dto.PlaceResponseDto;

public abstract class PlaceAssembler {

    private PlaceAssembler() {

    }

    public static PlaceResponse placeResponse(PlaceResponseDto placeResponseDto) {
        return PlaceResponse.builder()
            .placeId(placeResponseDto.getPlaceId())
            .position(placeResponseDto.getPosition())
            .myMemoryCount(placeResponseDto.getMyMemoryCount())
            .otherMemoryCount(placeResponseDto.getOtherMemoryCount())
            .isScraped(placeResponseDto.getIsScraped())
            .placeCategory(placeResponseDto.getPlaceCategory())
            .placeName(placeResponseDto.getPlaceName())
            .placeImage(placeResponseDto.getPlaceImage())
            .build();
    }

}
