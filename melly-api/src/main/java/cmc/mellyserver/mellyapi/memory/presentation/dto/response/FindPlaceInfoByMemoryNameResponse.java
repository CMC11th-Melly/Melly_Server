package cmc.mellyserver.mellyapi.memory.presentation.dto.response;

import lombok.Builder;
import lombok.Data;

@Data
public class FindPlaceInfoByMemoryNameResponse {
	private Long placeId;
	private String memoryName;

	@Builder
	public FindPlaceInfoByMemoryNameResponse(Long placeId, String memoryName) {
		this.placeId = placeId;
		this.memoryName = memoryName;
	}
}
