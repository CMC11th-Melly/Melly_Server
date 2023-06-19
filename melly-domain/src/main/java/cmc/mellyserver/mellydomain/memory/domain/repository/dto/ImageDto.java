package cmc.mellyserver.mellydomain.memory.domain.repository.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ImageDto {

    private Long imageId;
    private Long memoryId;
    private String memoryImage;
}
