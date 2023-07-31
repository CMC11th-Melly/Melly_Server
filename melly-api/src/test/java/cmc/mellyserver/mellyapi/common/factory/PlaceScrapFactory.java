package cmc.mellyserver.mellyapi.common.factory;

import cmc.mellyserver.mellyapi.scrap.presentation.dto.request.ScrapCancelRequest;
import cmc.mellyserver.mellyapi.scrap.presentation.dto.request.ScrapRequest;
import cmc.mellyserver.mellycommon.enums.ScrapType;
import cmc.mellyserver.mellycore.scrap.application.dto.request.CreatePlaceScrapRequestDto;
import cmc.mellyserver.mellycore.scrap.domain.PlaceScrap;

public class PlaceScrapFactory {

    public static ScrapRequest mockScrapRequest() {
        return ScrapRequest.builder()
                .lat(1.234)
                .lng(1.234)
                .scrapType(ScrapType.FRIEND)
                .placeCategory("카페")
                .placeName("스타벅스")
                .build();
    }

    public static ScrapCancelRequest mockScrapCancelRequest() {
        return ScrapCancelRequest.builder()
                .lat(1.234)
                .lng(1.234)
                .build();
    }

    public static CreatePlaceScrapRequestDto mockCreatePlaceScrapRequestDto() {
        return CreatePlaceScrapRequestDto.builder()
                .lat(1.234)
                .lng(1.234)
                .placeCategory("카페")
                .placeName("스타벅스")
                .scrapType(ScrapType.FRIEND)
                .id(1L)
                .build();
    }

    public static PlaceScrap mockPlaceScrap() {
        return PlaceScrap.builder()
                .scrapType(ScrapType.FRIEND)
                .user(null)
                .place(null)
                .build();
    }
}
