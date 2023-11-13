package cmc.mellyserver.controller.memory.dto.response;

import java.util.List;

import cmc.mellyserver.domain.group.query.dto.GroupListForSaveMemoryResponseDto;
import cmc.mellyserver.domain.memory.dto.response.MemoryImageDto;
import lombok.Builder;
import lombok.Data;

@Data
public class MemoryUpdateFormResponse {

	private List<MemoryImageDto> memoryImages;

	private String title;

	private String content;

	private List<GroupListForSaveMemoryResponseDto> userGroups;

	private Long star;

	private List<String> keywords;

	@Builder
	public MemoryUpdateFormResponse(List<MemoryImageDto> memoryImages, String title, String content,
		List<GroupListForSaveMemoryResponseDto> userGroups, Long star, List<String> keywords) {
		this.memoryImages = memoryImages;
		this.title = title;
		this.content = content;
		this.userGroups = userGroups;
		this.star = star;
		this.keywords = keywords;
	}

}
