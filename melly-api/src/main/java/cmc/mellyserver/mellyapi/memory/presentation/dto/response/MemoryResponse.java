package cmc.mellyserver.mellyapi.memory.presentation.dto.response;

import java.time.LocalDateTime;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonFormat;

import cmc.mellyserver.mellycore.common.enums.GroupType;
import cmc.mellyserver.mellycore.memory.domain.repository.dto.ImageDto;
import lombok.Builder;
import lombok.Data;

@Data
public class MemoryResponse {

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
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyyMMddHHmm")
	private LocalDateTime visitedDate;

	@Builder
	public MemoryResponse(Long placeId, String placeName, Long memoryId, List<ImageDto> imageDtos, String title,
		String content, GroupType groupType, String groupName, Long stars, List<String> keyword, boolean loginUserWrite,
		LocalDateTime visitedDate) {
		this.placeId = placeId;
		this.placeName = placeName;
		this.memoryId = memoryId;
		this.memoryImages = imageDtos;
		this.title = title;
		this.content = content;
		this.groupType = groupType;
		this.groupName = groupName;
		this.stars = stars;
		this.keyword = keyword;
		this.loginUserWrite = loginUserWrite;
		this.visitedDate = visitedDate;
	}
}
