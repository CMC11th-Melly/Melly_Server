package cmc.mellyserver.domain.memory.query.dto;

import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class FindPlaceByMemoryTitleResponseDto {

    private Long placeId;

    private String memoryName;

    @Builder
    public FindPlaceByMemoryTitleResponseDto(Long placeId, String memoryName) {
        this.placeId = placeId;
        this.memoryName = memoryName;
    }

}
