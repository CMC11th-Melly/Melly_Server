package cmc.mellyserver.domain.notification;

import org.springframework.stereotype.Component;

import cmc.mellyserver.dbcore.notification.Notification;
import cmc.mellyserver.dbcore.notification.NotificationRepository;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class NotificationWriter {

  private final NotificationRepository notificationRepository;

  public Notification save(Notification notification) {
	return notificationRepository.save(notification);
  }

}
