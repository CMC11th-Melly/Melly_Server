package cmc.mellyserver.dbcore.memory.query.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ImageDto implements Serializable {

    private Long imageId;
    private String memoryImage;
}
