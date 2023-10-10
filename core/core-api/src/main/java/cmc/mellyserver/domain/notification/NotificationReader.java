package cmc.mellyserver.domain.notification;

import cmc.mellyserver.dbcore.notification.Notification;
import cmc.mellyserver.dbcore.notification.NotificationRepository;
import cmc.mellyserver.support.exception.BusinessException;
import cmc.mellyserver.support.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class NotificationReader {

    private final NotificationRepository notificationRepository;

    public Notification findById(Long notificationId) {
        return notificationRepository.findById(notificationId).orElseThrow(() -> new BusinessException(ErrorCode.NO_SUCH_NOTIFICATION));
    }

    public List<Notification> getNotificationList(Long userId) {
        return notificationRepository.findAllByUserId(userId);
    }
}
