package cmc.mellyserver.controller.scrap.dto;

import cmc.mellyserver.controller.scrap.dto.request.ScrapRequest;
import cmc.mellyserver.domain.scrap.dto.request.CreatePlaceScrapRequestDto;

public abstract class ScrapAssembler {

	private ScrapAssembler() {
	}

	public static CreatePlaceScrapRequestDto createPlaceScrapRequestDto(Long id, ScrapRequest scrapRequest) {
		return CreatePlaceScrapRequestDto.builder()
			.id(id)
			.lat(scrapRequest.getLat())
			.lng(scrapRequest.getLng())
			.scrapType(scrapRequest.getScrapType())
			.placeName(scrapRequest.getPlaceName())
			.placeCategory(scrapRequest.getPlaceCategory())
			.build();
	}

}
