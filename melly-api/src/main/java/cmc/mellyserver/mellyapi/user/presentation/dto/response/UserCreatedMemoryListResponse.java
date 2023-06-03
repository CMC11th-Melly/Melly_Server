package cmc.mellyserver.mellyapi.user.presentation.dto.response;

import java.time.LocalDateTime;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonFormat;

import cmc.mellyserver.mellycore.common.enums.GroupType;
import cmc.mellyserver.mellycore.memory.domain.repository.dto.ImageDto;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class UserCreatedMemoryListResponse {

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
}
