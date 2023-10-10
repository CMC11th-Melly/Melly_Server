package cmc.mellyserver.domain.notification;

import cmc.mellyserver.dbcore.notification.Notification;
import cmc.mellyserver.dbcore.notification.NotificationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class NotificationWriter {

    private final NotificationRepository notificationRepository;

    public Notification save(Notification notification) {
        return notificationRepository.save(notification);
    }
}
