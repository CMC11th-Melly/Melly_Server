package cmc.mellyserver.mellydomain.notification.domain.repository;

import cmc.mellyserver.mellydomain.notification.domain.Notification;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface NotificationRepository extends JpaRepository<Notification, Long> {

    List<Notification> getNotificationByUserId(Long userId);
}
