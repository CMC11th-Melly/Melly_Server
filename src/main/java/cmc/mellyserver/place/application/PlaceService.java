package cmc.mellyserver.place.application;


import cmc.mellyserver.common.enums.GroupType;
import cmc.mellyserver.place.application.dto.PlaceResponseDto;
import cmc.mellyserver.place.application.dto.MarkedPlaceResponseDto;

import java.util.List;

public interface PlaceService {

    List<MarkedPlaceResponseDto> displayMarkedPlace(Long loginUserSeq, GroupType groupType);

    PlaceResponseDto findPlaceByPlaceId(Long loginUserSeq, Long placeId);

    PlaceResponseDto findPlaceByPosition(Long loginUserSeq, Double lat, Double lng);
}
