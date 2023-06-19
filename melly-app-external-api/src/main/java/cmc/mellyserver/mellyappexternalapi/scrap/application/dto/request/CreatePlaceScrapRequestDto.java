package cmc.mellyserver.mellyappexternalapi.scrap.application.dto.request;

import cmc.mellyserver.mellydomain.common.enums.ScrapType;
import lombok.Builder;
import lombok.Data;

@Data
public class CreatePlaceScrapRequestDto {

	private Long userSeq;

	private Double lat;

	private Double lng;

	private ScrapType scrapType;

	private String placeName;

	private String placeCategory;

	@Builder
	public CreatePlaceScrapRequestDto(Long userSeq, Double lat, Double lng, ScrapType scrapType, String placeName,
		String placeCategory) {
		this.userSeq = userSeq;
		this.lat = lat;
		this.lng = lng;
		this.scrapType = scrapType;
		this.placeName = placeName;
		this.placeCategory = placeCategory;
	}
}
