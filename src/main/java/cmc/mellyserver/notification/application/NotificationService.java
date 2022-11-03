package cmc.mellyserver.notification.application;

import cmc.mellyserver.common.exception.ExceptionCodeAndDetails;
import cmc.mellyserver.common.exception.GlobalBadRequestException;
import cmc.mellyserver.common.util.auth.AuthenticatedUserChecker;
import cmc.mellyserver.memory.domain.Memory;
import cmc.mellyserver.memory.domain.MemoryRepository;
import cmc.mellyserver.notification.domain.Notification;
import cmc.mellyserver.notification.domain.NotificationRepository;
import cmc.mellyserver.notification.domain.NotificationType;
import cmc.mellyserver.user.domain.User;
import cmc.mellyserver.user.presentation.dto.NotificationOnOffResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.w3c.dom.stylesheets.LinkStyle;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class NotificationService {

    private final AuthenticatedUserChecker authenticatedUserChecker;
    private final NotificationRepository notificationRepository;
    private final MemoryRepository memoryRepository;

    public List<Notification> getNotificationList(String uid)
    {
        User user = authenticatedUserChecker.checkAuthenticatedUserExist(uid);
        return user.getNotifications();
    }

    @Transactional
    public void setPushCommentLikeOn(String uid) {
        User user = authenticatedUserChecker.checkAuthenticatedUserExist(uid);
        user.setEnableCommentLike(true);
    }

    @Transactional
    public void setPushCommentLikeOff(String uid) {
        User user = authenticatedUserChecker.checkAuthenticatedUserExist(uid);
        user.setEnableCommentLike(false);
    }

    @Transactional
    public void setPushCommentOn(String uid) {
        User user = authenticatedUserChecker.checkAuthenticatedUserExist(uid);
        user.setEnableComment(true);
    }

    @Transactional
    public void setPushCommentOff(String uid) {
        User user = authenticatedUserChecker.checkAuthenticatedUserExist(uid);
        user.setEnableComment(false);
    }

    @Transactional
    public void setAppPushOn(String uid) {
        User user = authenticatedUserChecker.checkAuthenticatedUserExist(uid);
        user.setEnableAppPush(true);
    }

    @Transactional
    public void setAppPushOff(String uid) {
        User user = authenticatedUserChecker.checkAuthenticatedUserExist(uid);
        user.setEnableAppPush(false);
    }

    public NotificationOnOffResponse getNotificationOnOff(String uid) {
        User user = authenticatedUserChecker.checkAuthenticatedUserExist(uid);
        return new NotificationOnOffResponse(user.isEnableAppPush(),user.isEnableCommentLike(),user.isEnableComment());
    }

    @Transactional
    public Notification createNotification(NotificationType notificationType,String body,String uid, Long memoryId)
    {
        User user = authenticatedUserChecker.checkAuthenticatedUserExist(uid);
        Memory memory = memoryRepository.findById(memoryId).orElseThrow(() -> {
            throw new GlobalBadRequestException(ExceptionCodeAndDetails.NO_SUCH_MEMORY);
        });
        return notificationRepository.save(Notification.createNotification(notificationType,body,false,memory,user));
    }

    @Transactional
    public void checkNotification(Long notificationId) {

        Notification notification = notificationRepository.findById(notificationId).orElseThrow(() -> {
            throw new GlobalBadRequestException(ExceptionCodeAndDetails.NO_SUCH_NOTIFICATION);
        });
        notification.checkNotification(true);

    }
}
