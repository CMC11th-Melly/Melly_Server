package cmc.mellyserver.mellyapi.controller.scrap.dto;

import cmc.mellyserver.mellyapi.controller.scrap.dto.request.ScrapRequest;
import cmc.mellyserver.mellyapi.domain.scrap.dto.request.CreatePlaceScrapRequestDto;

public abstract class ScrapAssembler {

    public static CreatePlaceScrapRequestDto createPlaceScrapRequestDto(Long id,
                                                                        ScrapRequest scrapRequest) {
        return CreatePlaceScrapRequestDto.builder()
                .id(id)
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
