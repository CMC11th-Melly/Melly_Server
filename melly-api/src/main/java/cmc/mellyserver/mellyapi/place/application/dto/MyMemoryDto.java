package cmc.mellyserver.mellyapi.place.application.dto;

import java.util.List;

import cmc.mellyserver.mellycore.common.enums.GroupType;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class MyMemoryDto {
	private Long memoryId;
	private GroupType groupType;
	private List<MemoryImageDto> memoryImages;
	private String title;
	private List<String> keyword;
	private String createdDate;
}
