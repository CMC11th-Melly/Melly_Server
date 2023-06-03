package cmc.mellyserver.mellyapi.place.application;

import java.util.List;

import cmc.mellyserver.mellyapi.place.application.dto.MarkedPlaceResponseDto;
import cmc.mellyserver.mellyapi.place.application.dto.PlaceResponseDto;
import cmc.mellyserver.mellycore.common.enums.GroupType;

public interface PlaceService {

	List<MarkedPlaceResponseDto> displayMarkedPlace(Long loginUserSeq, GroupType groupType);

	PlaceResponseDto findPlaceByPlaceId(Long loginUserSeq, Long placeId);

	PlaceResponseDto findPlaceByPosition(Long loginUserSeq, Double lat, Double lng);
}
