package cmc.mellyserver.notification.application;

import cmc.mellyserver.common.util.auth.AuthenticatedUserChecker;
import cmc.mellyserver.notification.domain.Notification;
import cmc.mellyserver.notification.domain.NotificationRepository;
import cmc.mellyserver.user.domain.User;
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
}
