package cmc.mellyserver.mellyapi.notification.presentation.dto;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class NotificationWrapper {

	private List<NotificationResponse> notifications;

}
