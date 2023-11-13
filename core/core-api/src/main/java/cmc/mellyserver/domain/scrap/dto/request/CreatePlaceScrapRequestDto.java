package cmc.mellyserver.domain.scrap.dto.request;

import cmc.mellyserver.dbcore.place.Place;
import cmc.mellyserver.dbcore.place.Position;
import cmc.mellyserver.dbcore.scrap.ScrapType;
import lombok.Builder;
import lombok.Data;

@Data
public class CreatePlaceScrapRequestDto {

	private Long id;

	private Position position;

	private ScrapType scrapType;

	private String placeName;

	private String placeCategory;

	@Builder
	public CreatePlaceScrapRequestDto(Long id, Double lat, Double lng, ScrapType scrapType, String placeName,
		String placeCategory) {
		this.id = id;
		this.position = new Position(lat, lng);
		this.scrapType = scrapType;
		this.placeName = placeName;
		this.placeCategory = placeCategory;
	}

	public Place toEntity() {
		return Place.builder()
			.name(placeName)
			.category(placeCategory)
			.position(position)
			.build();
	}

}
