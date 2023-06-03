package cmc.mellyserver.mellyapi.user.presentation.dto.response;

import cmc.mellyserver.mellycore.common.enums.GroupType;
import cmc.mellyserver.mellycore.place.domain.Position;
import lombok.Builder;
import lombok.Data;

@Data
public class ScrapedPlaceResponse {

	private Long placeId = -1L;
	private Position position;
	private Long myMemoryCount;
	private Long otherMemoryCount;
	private Boolean isScraped;
	private String placeCategory = "";
	private String placeName = "";
	private GroupType recommendType = GroupType.ALL;
	private String placeImage;

	public ScrapedPlaceResponse(Long placeId, Position position, String placeCategory, String placeName,
		String placeImage) {
		this.placeId = placeId;
		this.position = position;
		this.placeCategory = placeCategory;
		this.placeName = placeName;
		this.placeImage = placeImage;
	}

	@Builder
	public ScrapedPlaceResponse(Long placeId, Position position, Long myMemoryCount, Long otherMemoryCount,
		Boolean isScraped, String placeCategory, String placeName, GroupType recommendType, String placeImage) {
		this.placeId = placeId;
		this.position = position;
		this.myMemoryCount = myMemoryCount;
		this.otherMemoryCount = otherMemoryCount;
		this.isScraped = isScraped;
		this.placeCategory = placeCategory;
		this.placeName = placeName;
		this.recommendType = recommendType;
		this.placeImage = placeImage;
	}
}
