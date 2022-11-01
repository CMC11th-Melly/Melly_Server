package cmc.mellyserver.common.event;

import cmc.mellyserver.notification.domain.NotificationType;
import lombok.Getter;
import org.springframework.context.ApplicationEvent;

@Getter
public class CustomEvent extends ApplicationEvent {

    private NotificationType notificationType;
    private String message;
    private String fcmToken;

    public CustomEvent(Object source,String fcmToken, NotificationType notificationType,String message) {
        super(source);
        this.notificationType = notificationType;
        this.message = message;
        this.fcmToken = fcmToken;
    }
}
