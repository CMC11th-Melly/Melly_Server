package cmc.mellyserver.domain.scrap.query.dto;

import cmc.mellyserver.dbcore.place.Position;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class ScrapedPlaceResponseDto {

	private Long placeId;

	private Position position;

	private String placeCategory;

	private String placeName;

	private String placeImage;

}
