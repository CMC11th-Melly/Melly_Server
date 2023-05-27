package cmc.mellyserver.notification.application;

import cmc.mellyserver.common.exception.ExceptionCodeAndDetails;
import cmc.mellyserver.common.exception.GlobalBadRequestException;
import cmc.mellyserver.common.util.auth.AuthenticatedUserChecker;
import cmc.mellyserver.memory.domain.Memory;
import cmc.mellyserver.memory.domain.repository.MemoryRepository;
import cmc.mellyserver.notification.domain.Notification;
import cmc.mellyserver.notification.domain.repository.NotificationRepository;
import cmc.mellyserver.common.enums.NotificationType;
import cmc.mellyserver.user.domain.User;
import cmc.mellyserver.user.application.dto.response.NotificationOnOffResponseDto;
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


    public List<Notification> getNotificationList(Long userSeq)
    {
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
        return new NotificationOnOffResponseDto(user.isEnableAppPush(),user.isEnableCommentLike(),user.isEnableComment());
    }

    @Transactional
    public Notification createNotification(NotificationType notificationType,String body,Long userSeq, Long memoryId)
    {
        User user = authenticatedUserChecker.checkAuthenticatedUserExist(userSeq);
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
