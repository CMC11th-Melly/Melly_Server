package cmc.mellyserver.mellyappexternalapi.common.factory;

import cmc.mellyserver.mellyappexternalapi.scrap.application.dto.request.CreatePlaceScrapRequestDto;
import cmc.mellyserver.mellyappexternalapi.scrap.presentation.dto.request.ScrapCancelRequest;
import cmc.mellyserver.mellyappexternalapi.scrap.presentation.dto.request.ScrapRequest;
import cmc.mellyserver.mellydomain.common.enums.ScrapType;
import cmc.mellyserver.mellydomain.scrap.domain.PlaceScrap;

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
			.userSeq(1L)
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