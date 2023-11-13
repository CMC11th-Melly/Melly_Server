package cmc.mellyserver.controller.notification.dto.response;

import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonFormat;

import cmc.mellyserver.dbcore.notification.enums.NotificationType;
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

}
