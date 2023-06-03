package cmc.mellyserver.mellyapi.place.application.dto;

import java.util.List;

import cmc.mellyserver.mellycore.common.enums.GroupType;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class OtherMemoryDto {
	private Long memoryId;
	private GroupType groupType;
	private List<MemoryImageDto> memoryImages;
	private String title;
	private List<String> keyword;
	private String createdDate;
}
