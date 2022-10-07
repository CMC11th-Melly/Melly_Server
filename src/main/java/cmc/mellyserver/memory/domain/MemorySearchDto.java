package cmc.mellyserver.memory.domain;

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
