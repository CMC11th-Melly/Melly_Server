package cmc.mellyserver.memory.presentation.dto.common;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ImageDto {

    private Long memoryId;
    private Long imageId;
    private String memoryImage;
}
