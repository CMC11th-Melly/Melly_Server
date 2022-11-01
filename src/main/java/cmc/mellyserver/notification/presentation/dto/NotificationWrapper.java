package cmc.mellyserver.notification.presentation.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class NotificationWrapper {

    private List<NotificationResponse> notifications;

}
