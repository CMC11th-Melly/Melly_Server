package cmc.mellyserver.mellyappexternalapi.scrap.presentation.dto.request;

import cmc.mellyserver.mellyappexternalapi.scrap.application.dto.request.CreatePlaceScrapRequestDto;

public abstract class ScrapAssembler {

	public static CreatePlaceScrapRequestDto createPlaceScrapRequestDto(Long userSeq, ScrapRequest scrapRequest) {
		return CreatePlaceScrapRequestDto.builder()
			.userSeq(userSeq)
			.lat(scrapRequest.getLat())
			.lng(scrapRequest.getLng())
			.scrapType(scrapRequest.getScrapType())
			.placeName(scrapRequest.getPlaceName())
			.placeCategory(scrapRequest.getPlaceCategory())
			.build();
	}

	private ScrapAssembler() {
	}
}
