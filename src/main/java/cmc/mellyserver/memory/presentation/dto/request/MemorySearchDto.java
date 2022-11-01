package cmc.mellyserver.memory.presentation.dto.request;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MemorySearchDto {
    private Long placeId;
    private String memoryName;
}
