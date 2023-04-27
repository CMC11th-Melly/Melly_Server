package cmc.mellyserver.notification.domain.repository;

import cmc.mellyserver.notification.domain.Notification;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface NotificationRepository extends JpaRepository<Notification,Long> {

    List<Notification> getNotificationByUserId(Long userId);
}
