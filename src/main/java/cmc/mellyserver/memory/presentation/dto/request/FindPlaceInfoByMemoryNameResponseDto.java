package cmc.mellyserver.memory.presentation.dto.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class FindPlaceInfoByMemoryNameResponseDto {
    private Long placeId;
    private String memoryName;
}
