package cmc.mellyserver.place.application;


import cmc.mellyserver.common.enums.GroupType;
import cmc.mellyserver.place.application.dto.PlaceResponseDto;
import cmc.mellyserver.place.presentation.dto.response.MarkedPlaceReponseDto;

import java.util.List;

public interface PlaceService {

    List<MarkedPlaceReponseDto> displayMarkedPlace(Long loginUserSeq, GroupType groupType);

    PlaceResponseDto findPlaceByPlaceId(Long loginUserSeq, Long placeId);

    PlaceResponseDto findPlaceByPosition(Long loginUserSeq, Double lat, Double lng);
}
