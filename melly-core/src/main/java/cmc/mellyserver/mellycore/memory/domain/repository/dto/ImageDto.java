package cmc.mellyserver.mellycore.memory.domain.repository.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ImageDto {

	private Long imageId;
	private Long memoryId;
	private String memoryImage;
}
