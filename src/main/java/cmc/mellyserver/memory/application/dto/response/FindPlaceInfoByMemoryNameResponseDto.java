package cmc.mellyserver.memory.application.dto.response;

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
