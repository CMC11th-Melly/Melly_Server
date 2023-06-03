package cmc.mellyserver.mellyapi.place.presentation.dto;

import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

import cmc.mellyserver.mellyapi.place.application.dto.MarkedPlaceResponseDto;
import cmc.mellyserver.mellyapi.place.application.dto.PlaceResponseDto;
import cmc.mellyserver.mellyapi.place.presentation.dto.response.MarkedPlaceReponse;
import cmc.mellyserver.mellyapi.place.presentation.dto.response.PlaceResponse;
import cmc.mellyserver.mellycore.common.enums.GroupType;
import cmc.mellyserver.mellycore.place.domain.Place;

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

	public static PlaceResponseDto placeResponseDto(Place place, Boolean isScraped,
		HashMap<String, Long> memoryCounts) {
		return new PlaceResponseDto(place.getId(), place.getPosition(), memoryCounts.get("belongToUSer"),
			memoryCounts.get("NotBelongToUSer"),
			isScraped, place.getPlaceCategory(), place.getPlaceName(), GroupType.ALL, place.getPlaceImage());
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
}
