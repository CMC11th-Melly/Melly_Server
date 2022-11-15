package cmc.mellyserver.memory.application.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class MemoryImageDto {

    private Long imageId;
    private String memoryImage;
}
