package cmc.mellyserver.mellyapi.controller.notification.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class NotificationWrapper {

    private List<NotificationResponse> notifications;

}
