package cmc.mellyserver.mellycore.scrap.domain.repository.dto;

import cmc.mellyserver.mellycommon.enums.GroupType;
import cmc.mellyserver.mellycore.place.domain.Position;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ScrapedPlaceResponseDto {

	private Long placeId = -1L;
	private Position position;
	private Long myMemoryCount;
	private Long otherMemoryCount;
	private Boolean isScraped;
	private String placeCategory = "";
	private String placeName = "";
	private GroupType recommendType = GroupType.ALL;
	private String placeImage;

	public ScrapedPlaceResponseDto(Long placeId, Position position, String placeCategory, String placeName,
		String placeImage) {
		this.placeId = placeId;
		this.position = position;
		this.placeCategory = placeCategory;
		this.placeName = placeName;
		this.placeImage = placeImage;
	}
}
