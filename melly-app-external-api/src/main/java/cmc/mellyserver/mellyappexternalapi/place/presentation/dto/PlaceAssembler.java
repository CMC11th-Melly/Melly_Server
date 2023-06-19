package cmc.mellyserver.mellyappexternalapi.place.presentation.dto;

import java.util.List;
import java.util.stream.Collectors;

import cmc.mellyserver.mellyappexternalapi.place.application.dto.MarkedPlaceResponseDto;
import cmc.mellyserver.mellyappexternalapi.place.application.dto.PlaceResponseDto;
import cmc.mellyserver.mellyappexternalapi.place.presentation.dto.response.MarkedPlaceReponse;
import cmc.mellyserver.mellyappexternalapi.place.presentation.dto.response.PlaceResponse;

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
