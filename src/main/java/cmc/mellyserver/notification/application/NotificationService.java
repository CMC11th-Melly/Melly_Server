package cmc.mellyserver.notification.application;

import cmc.mellyserver.common.util.auth.AuthenticatedUserChecker;
import cmc.mellyserver.notification.domain.Notification;
import cmc.mellyserver.notification.domain.NotificationRepository;
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

    private final NotificationRepository notificationRepository;
    private final AuthenticatedUserChecker authenticatedUserChecker;

    public List<Notification> getNotificationList(String uid)
    {
        User user = authenticatedUserChecker.checkAuthenticatedUserExist(uid);
        return notificationRepository.getNotificationByUserUserSeq(user.getUserSeq());
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
}
