package cmc.mellyserver.mellycore.notification.domain.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import cmc.mellyserver.mellycore.notification.domain.Notification;

public interface NotificationRepository extends JpaRepository<Notification, Long> {

	List<Notification> getNotificationByUserId(Long userId);
}
