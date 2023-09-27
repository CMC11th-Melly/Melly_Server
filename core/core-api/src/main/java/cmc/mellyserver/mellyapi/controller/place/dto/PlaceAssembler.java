package cmc.mellyserver.mellyapi.controller.place.dto;

import cmc.mellyserver.mellyapi.controller.place.dto.response.MarkedPlaceReponse;
import cmc.mellyserver.mellyapi.controller.place.dto.response.PlaceResponse;
import cmc.mellyserver.mellyapi.domain.scrap.dto.MarkedPlaceResponseDto;
import cmc.mellyserver.mellyapi.domain.scrap.dto.PlaceResponseDto;

import java.util.List;
import java.util.stream.Collectors;

public abstract class PlaceAssembler {

    public static List<MarkedPlaceReponse> markedPlaceReponse(List<MarkedPlaceResponseDto> markedPlaceReponseDtos) {
        return markedPlaceReponseDtos.stream()
                .map(each -> MarkedPlaceReponse.builder()
                        .placeId(each.getPlaceId())
                        .position(each.getPosition())
                        .groupType(each.getGroupType())
                        .memoryCount(each.getMemoryCount())
                        .build())
                .collect(Collectors.toList());
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
                .recommendType(placeResponseDto.getRecommendType())
                .placeImage(placeResponseDto.getPlaceImage())
                .build();
    }

    private PlaceAssembler() {

    }
}
