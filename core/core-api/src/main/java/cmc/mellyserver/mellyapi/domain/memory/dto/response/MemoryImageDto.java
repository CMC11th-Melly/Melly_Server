package cmc.mellyserver.mellyapi.domain.memory.dto.response;



import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class MemoryImageDto {

    private Long imageId;
    private String memoryImage;
}
