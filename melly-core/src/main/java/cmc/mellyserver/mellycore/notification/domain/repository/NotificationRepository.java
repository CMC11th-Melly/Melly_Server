package cmc.mellyserver.mellycore.notification.domain.repository;

import cmc.mellyserver.mellycore.notification.domain.Notification;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface NotificationRepository extends MongoRepository<Notification, Long> {

}
