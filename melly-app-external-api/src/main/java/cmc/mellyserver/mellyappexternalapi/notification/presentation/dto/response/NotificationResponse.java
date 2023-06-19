package cmc.mellyserver.mellyappexternalapi.notification.presentation.dto.response;

import java.time.LocalDateTime;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonFormat;

import cmc.mellyserver.mellydomain.common.enums.GroupType;
import cmc.mellyserver.mellydomain.common.enums.NotificationType;
import cmc.mellyserver.mellydomain.memory.domain.repository.dto.ImageDto;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class NotificationResponse {

	private Long notificationId;
	private NotificationType type;
	private String content;
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyyMMddHHmm")
	private LocalDateTime date;
	private boolean checked;
	private NotificationMemoryDto memory;

	@Data
	@AllArgsConstructor
	static class NotificationMemoryDto {
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
}
