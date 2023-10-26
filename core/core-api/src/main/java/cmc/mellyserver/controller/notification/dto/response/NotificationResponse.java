package cmc.mellyserver.controller.notification.dto.response;

import cmc.mellyserver.dbcore.notification.enums.NotificationType;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDateTime;

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
