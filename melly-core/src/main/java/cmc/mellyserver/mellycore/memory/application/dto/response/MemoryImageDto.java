package cmc.mellyserver.mellycore.memory.application.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class MemoryImageDto {

    private Long imageId;
    private String memoryImage;
}
