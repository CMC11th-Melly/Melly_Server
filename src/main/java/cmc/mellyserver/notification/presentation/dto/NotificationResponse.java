package cmc.mellyserver.notification.presentation.dto;

import cmc.mellyserver.notification.domain.NotificationType;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class NotificationResponse {

    private NotificationType title;
    private String body;
    private boolean checked;

}
