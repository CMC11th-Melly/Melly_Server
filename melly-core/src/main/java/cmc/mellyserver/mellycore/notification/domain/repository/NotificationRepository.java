package cmc.mellyserver.mellycore.notification.domain.repository;

import cmc.mellyserver.mellycore.notification.domain.Notification;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface NotificationRepository extends JpaRepository<Notification, Long> {

    List<Notification> getNotificationByUserId(Long userId);
}
