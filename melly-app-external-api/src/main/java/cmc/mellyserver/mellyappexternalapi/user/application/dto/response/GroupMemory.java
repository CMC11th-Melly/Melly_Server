package cmc.mellyserver.mellyappexternalapi.user.application.dto.response;

import java.time.LocalDateTime;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonFormat;

import cmc.mellyserver.mellydomain.common.enums.GroupType;
import cmc.mellyserver.mellydomain.memory.domain.repository.dto.ImageDto;
import lombok.Data;

@Data
public class GroupMemory {
	private Long placeId;
	private String placeName;
	private Long memoryId;
	private List<ImageDto> memoryImages;
	private String title;
	private String content;
	private GroupType groupType;
	private String groupName;
	private Long stars;
	private boolean loginUserWrite;
	private List<String> keyword;
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyyMMddHHmm")
	private LocalDateTime visitedDate;

}
