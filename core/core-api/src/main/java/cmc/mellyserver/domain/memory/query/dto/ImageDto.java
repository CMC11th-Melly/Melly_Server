package cmc.mellyserver.domain.memory.query.dto;

import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ImageDto implements Serializable {

    private Long imageId;

    private String memoryImage;

}
