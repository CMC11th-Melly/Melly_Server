package cmc.mellyserver.notification.presentation.dto;

import cmc.mellyserver.notification.domain.Notification;

import java.util.List;
import java.util.stream.Collectors;

public class NotificationAssembler {

    public static List<NotificationResponse> notificationResponses(List<Notification> notifications)
    {
        return notifications.stream().map(noti -> new NotificationResponse(noti.getTitle(),noti.getMessage(),noti.isChecked())).collect(Collectors.toList());
    }
}
