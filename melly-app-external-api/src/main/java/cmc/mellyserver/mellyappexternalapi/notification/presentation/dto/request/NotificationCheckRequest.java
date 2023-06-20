package cmc.mellyserver.mellyappexternalapi.notification.presentation.dto.request;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class NotificationCheckRequest {
	private Long notificationId;

	public NotificationCheckRequest(Long notificationId) {
		this.notificationId = notificationId;
	}
}
