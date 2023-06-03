package cmc.mellyserver.mellycore.memory.domain.repository.dto;

import java.time.LocalDateTime;
import java.util.List;

import cmc.mellyserver.mellycore.common.enums.GroupType;
import lombok.Builder;
import lombok.Data;

/**
 * UserCreatedMemoryListResponseDto.java
 *
 * @author jemlog
 */
@Data
public class MemoryResponseDto {

	private Long placeId;

	private String placeName;

	private Long memoryId;

	private List<ImageDto> memoryImages;
	private String title;

	private String content;

	private GroupType groupType;

	private String groupName;

	private Long stars;

	private List<String> keyword;
	private boolean loginUserWrite;

	private LocalDateTime visitedDate;

	@Builder
	public MemoryResponseDto(Long placeId, String placeName, Long memoryId, String title, String content,
		GroupType groupType, String groupName, Long stars, boolean loginUserWrite, LocalDateTime visitedDate) {
		this.placeId = placeId;
		this.placeName = placeName;
		this.memoryId = memoryId;
		this.title = title;
		this.content = content;
		this.groupType = groupType;
		this.groupName = groupName;
		this.stars = stars;
		this.loginUserWrite = loginUserWrite;
		this.visitedDate = visitedDate;
	}
}
