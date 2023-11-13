package cmc.mellyserver.domain.notification;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Component;

import cmc.mellyserver.controller.notification.dto.response.NotificationResponse;
import cmc.mellyserver.dbcore.notification.Notification;
import cmc.mellyserver.dbcore.notification.NotificationRepository;
import cmc.mellyserver.support.exception.BusinessException;
import cmc.mellyserver.support.exception.ErrorCode;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class NotificationReader {

  private final NotificationRepository notificationRepository;

  public Notification findById(Long notificationId) {
	return notificationRepository.findById(notificationId)
		.orElseThrow(() -> new BusinessException(ErrorCode.NO_SUCH_NOTIFICATION));
  }

  public List<NotificationResponse> getNotificationList(Long userId) {
	List<Notification> notifications = notificationRepository.findAllByUserId(userId);
	return notifications.stream()
		.map(noti -> new NotificationResponse(noti.getId(), noti.getNotificationType(), noti.getContent(),
			noti.getCreatedDate(), noti.isRead()))
		.collect(Collectors.toList());
  }

}
