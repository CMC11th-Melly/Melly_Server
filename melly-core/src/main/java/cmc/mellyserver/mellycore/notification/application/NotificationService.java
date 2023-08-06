package cmc.mellyserver.mellycore.notification.application;


import cmc.mellyserver.mellycommon.codes.ErrorCode;
import cmc.mellyserver.mellycommon.enums.NotificationType;
import cmc.mellyserver.mellycore.common.exception.BusinessException;
import cmc.mellyserver.mellycore.common.util.auth.AuthenticatedUserChecker;
import cmc.mellyserver.mellycore.memory.domain.Memory;
import cmc.mellyserver.mellycore.memory.domain.repository.MemoryRepository;
import cmc.mellyserver.mellycore.notification.application.dto.response.NotificationOnOffResponseDto;
import cmc.mellyserver.mellycore.notification.domain.Notification;
import cmc.mellyserver.mellycore.notification.domain.repository.NotificationRepository;
import cmc.mellyserver.mellycore.user.domain.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class NotificationService {

    private final AuthenticatedUserChecker authenticatedUserChecker;

    private final NotificationRepository notificationRepository;

    private final MemoryRepository memoryRepository;


    // TODO : 알림 부분에도 캐싱 적용을 할지 고민해보기
    @Transactional(readOnly = true)
    public List<Notification> getNotificationList(Long userId) {
        User user = authenticatedUserChecker.checkAuthenticatedUserExist(userId);
        return notificationRepository.getNotificationByUserId(user.getId());
    }

    @Transactional(readOnly = true)
    public NotificationOnOffResponseDto getNotificationStatus(Long userId) {
        User user = authenticatedUserChecker.checkAuthenticatedUserExist(userId);
        return NotificationOnOffResponseDto.of(user.getEnableAppPush(), user.getEnableCommentPush(), user.getEnableCommentLikePush());
    }

    @Transactional
    public void changeAppPushStatus(Long userId, boolean status) {
        User user = authenticatedUserChecker.checkAuthenticatedUserExist(userId);
        user.changeAppPushStatus(status);
    }

    @Transactional
    public void changeCommentLikePushStatus(Long userId, boolean status) {
        User user = authenticatedUserChecker.checkAuthenticatedUserExist(userId);
        user.changeCommentLikePushStatus(status);
    }

    @Transactional
    public void changeCommentPushStatus(Long userId, boolean status) {
        User user = authenticatedUserChecker.checkAuthenticatedUserExist(userId);
        user.changeCommenPushStatus(status);
    }


    @Transactional
    public Notification createNotification(String title, String body, NotificationType notificationType, Long userId, Long memoryId) {

        User user = authenticatedUserChecker.checkAuthenticatedUserExist(userId);
        Memory memory = memoryRepository.findById(memoryId).orElseThrow(() -> new BusinessException(ErrorCode.NO_SUCH_MEMORY));
        return notificationRepository.save(Notification.createNotification(title, body, notificationType, false, memory.getId(), user.getId()));
    }

    @Transactional
    public void checkNotification(Long notificationId) {

        Notification notification = notificationRepository.findById(notificationId).orElseThrow(() -> {
            throw new BusinessException(ErrorCode.NO_SUCH_NOTIFICATION);
        });
        notification.userCheckedNotification();
    }
}
