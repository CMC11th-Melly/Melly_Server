package cmc.mellyserver.mellycore.notification.domain.repository;

import cmc.mellyserver.mellycore.notification.domain.Notification;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface NotificationRepository extends MongoRepository<Notification, Long> {

    List<Notification> findAllByUserId(Long userId);
}
