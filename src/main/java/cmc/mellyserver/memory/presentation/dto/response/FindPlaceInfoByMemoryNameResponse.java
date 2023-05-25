package cmc.mellyserver.memory.presentation.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

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
