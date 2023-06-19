package cmc.mellyserver.mellyappexternalapi.place.presentation.dto.response;

import cmc.mellyserver.mellydomain.common.enums.GroupType;
import cmc.mellyserver.mellydomain.place.domain.Position;
import lombok.Builder;
import lombok.Data;

@Data
public class MarkedPlaceReponse {

	private Position position;
	private GroupType groupType;
	private Long placeId;
	private Long memoryCount;

	@Builder
	public MarkedPlaceReponse(Position position, GroupType groupType, Long placeId, Long memoryCount) {
		this.position = position;
		this.groupType = groupType;
		this.placeId = placeId;
		this.memoryCount = memoryCount;
	}
}
