package cmc.mellyserver.common.factory;

import cmc.mellyserver.common.enums.ScrapType;
import cmc.mellyserver.scrap.application.dto.request.CreatePlaceScrapRequestDto;
import cmc.mellyserver.scrap.domain.PlaceScrap;
import cmc.mellyserver.scrap.presentation.dto.request.ScrapCancelRequest;
import cmc.mellyserver.scrap.presentation.dto.request.ScrapRequest;

public class PlaceScrapFactory {

    public static ScrapRequest mockScrapRequest()
    {
        return ScrapRequest.builder()
                .lat(1.234)
                .lng(1.234)
                .scrapType(ScrapType.FRIEND)
                .placeCategory("카페")
                .placeName("스타벅스")
                .build();
    }

    public static ScrapCancelRequest mockScrapCancelRequest()
    {
        return ScrapCancelRequest.builder()
                .lat(1.234)
                .lng(1.234)
                .build();
    }

    public static CreatePlaceScrapRequestDto mockCreatePlaceScrapRequestDto()
    {
        return CreatePlaceScrapRequestDto.builder()
                .lat(1.234)
                .lng(1.234)
                .placeCategory("카페")
                .placeName("스타벅스")
                .scrapType(ScrapType.FRIEND)
                .userSeq(1L)
                .build();
    }

    public static PlaceScrap mockPlaceScrap()
    {
        return PlaceScrap.builder()
                .scrapType(ScrapType.FRIEND)
                .user(null)
                .place(null)
                .build();
    }
}
