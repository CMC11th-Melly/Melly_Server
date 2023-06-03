package cmc.mellyserver.mellyapi.notification.application;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cmc.mellyserver.mellyapi.common.auth.AuthenticatedUserChecker;
import cmc.mellyserver.mellyapi.common.exception.ExceptionCodeAndDetails;
import cmc.mellyserver.mellyapi.common.exception.GlobalBadRequestException;
import cmc.mellyserver.mellyapi.user.application.dto.response.NotificationOnOffResponseDto;
import cmc.mellyserver.mellycore.common.enums.NotificationType;
import cmc.mellyserver.mellycore.memory.domain.Memory;
import cmc.mellyserver.mellycore.memory.domain.repository.MemoryRepository;
import cmc.mellyserver.mellycore.notification.domain.Notification;
import cmc.mellyserver.mellycore.notification.domain.repository.NotificationRepository;
import cmc.mellyserver.mellycore.user.domain.User;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class NotificationService {

	private final AuthenticatedUserChecker authenticatedUserChecker;
	private final NotificationRepository notificationRepository;
	private final MemoryRepository memoryRepository;

	public List<Notification> getNotificationList(Long userSeq) {
		User user = authenticatedUserChecker.checkAuthenticatedUserExist(userSeq);
		return null;
	}

	@Transactional
	public void setPushCommentLikeOn(Long userSeq) {
		User user = authenticatedUserChecker.checkAuthenticatedUserExist(userSeq);
		user.setEnableCommentLike(true);
	}

	@Transactional
	public void setPushCommentLikeOff(Long userSeq) {
		User user = authenticatedUserChecker.checkAuthenticatedUserExist(userSeq);
		user.setEnableCommentLike(false);
	}

	@Transactional
	public void setPushCommentOn(Long userSeq) {
		User user = authenticatedUserChecker.checkAuthenticatedUserExist(userSeq);
		user.setEnableComment(true);
	}

	@Transactional
	public void setPushCommentOff(Long userSeq) {
		User user = authenticatedUserChecker.checkAuthenticatedUserExist(userSeq);
		user.setEnableComment(false);
	}

	@Transactional
	public void setAppPushOn(Long userSeq) {
		User user = authenticatedUserChecker.checkAuthenticatedUserExist(userSeq);
		user.setEnableAppPush(true);
	}

	@Transactional
	public void setAppPushOff(Long userSeq) {
		User user = authenticatedUserChecker.checkAuthenticatedUserExist(userSeq);
		user.setEnableAppPush(false);
	}

	public NotificationOnOffResponseDto getNotificationOnOff(Long userSeq) {
		User user = authenticatedUserChecker.checkAuthenticatedUserExist(userSeq);
		return new NotificationOnOffResponseDto(user.isEnableAppPush(), user.isEnableCommentLike(),
			user.isEnableComment());
	}

	@Transactional
	public Notification createNotification(NotificationType notificationType, String body, Long userSeq,
		Long memoryId) {
		User user = authenticatedUserChecker.checkAuthenticatedUserExist(userSeq);
		Memory memory = memoryRepository.findById(memoryId).orElseThrow(() -> {
			throw new GlobalBadRequestException(ExceptionCodeAndDetails.NO_SUCH_MEMORY);
		});
		return notificationRepository.save(
			Notification.createNotification(notificationType, body, false, memory, user));
	}

	@Transactional
	public void checkNotification(Long notificationId) {

		Notification notification = notificationRepository.findById(notificationId).orElseThrow(() -> {
			throw new GlobalBadRequestException(ExceptionCodeAndDetails.NO_SUCH_NOTIFICATION);
		});
		notification.checkNotification(true);

	}
}
