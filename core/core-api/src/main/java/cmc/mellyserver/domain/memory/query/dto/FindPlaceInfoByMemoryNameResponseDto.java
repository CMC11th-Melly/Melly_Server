package cmc.mellyserver.domain.memory.query.dto;

import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class FindPlaceInfoByMemoryNameResponseDto {
    private Long placeId;
    private String memoryName;

    @Builder
    public FindPlaceInfoByMemoryNameResponseDto(Long placeId, String memoryName) {
        this.placeId = placeId;
        this.memoryName = memoryName;
    }
}