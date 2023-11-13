package cmc.mellyserver.domain.notification;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cmc.mellyserver.controller.notification.dto.response.NotificationResponse;
import cmc.mellyserver.dbcore.memory.Memory;
import cmc.mellyserver.dbcore.notification.Notification;
import cmc.mellyserver.dbcore.notification.enums.NotificationType;
import cmc.mellyserver.dbcore.user.User;
import cmc.mellyserver.domain.memory.MemoryReader;
import cmc.mellyserver.domain.notification.dto.response.NotificationOnOffResponseDto;
import cmc.mellyserver.domain.user.UserReader;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class NotificationService {

  private final NotificationReader notificationReader;

  private final NotificationWriter notificationWriter;

  private final MemoryReader memoryReader;

  private final UserReader userReader;

  public List<NotificationResponse> getNotificationList(Long userId) {
	return notificationReader.getNotificationList(userId);
  }

  public NotificationOnOffResponseDto getNotificationStatus(Long userId) {

	User user = userReader.findById(userId);
	return NotificationOnOffResponseDto.of(user.isEnableAppPush(), user.isEnableCommentPush(),
		user.isEnableCommentLikePush());
  }

  @Transactional
  public void changeAppPushStatus(Long userId, boolean status) {

	User user = userReader.findById(userId);
	user.changeAppPushStatus(status);
  }

  @Transactional
  public void changeCommentLikePushStatus(Long userId, boolean status) {

	User user = userReader.findById(userId);
	user.changeCommentLikePushStatus(status);
  }

  @Transactional
  public void changeCommentPushStatus(Long userId, boolean status) {

	User user = userReader.findById(userId);
	user.changeCommentPushStatus(status);
  }

  @Transactional
  public Notification createNotification(String body, NotificationType notificationType, Long userId, Long memoryId) {

	User user = userReader.findById(userId);
	Memory memory = memoryReader.findById(memoryId);
	return notificationWriter.save(Notification.createNotification(body, userId, notificationType, false,
		user.getProfileImage(), user.getNickname(), memory.getId()));
  }

  @Transactional
  public void readNotification(Long notificationId) {

	Notification notification = notificationReader.findById(notificationId);
	notification.read();
  }

}
