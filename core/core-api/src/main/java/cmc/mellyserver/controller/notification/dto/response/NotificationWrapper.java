package cmc.mellyserver.controller.notification.dto.response;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class NotificationWrapper {

    private List<NotificationResponse> notifications;

}
