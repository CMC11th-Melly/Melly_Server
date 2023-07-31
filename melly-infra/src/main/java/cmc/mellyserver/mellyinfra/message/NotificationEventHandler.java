package cmc.mellyserver.mellyinfra.message;

import cmc.mellyserver.mellycore.comment.application.event.CommentEnrollEvent;
import cmc.mellyserver.mellycore.comment.application.event.CommentLikeEvent;
import cmc.mellyserver.mellycore.common.AuthenticatedUserChecker;
import cmc.mellyserver.mellycore.user.domain.User;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionalEventListener;

@Slf4j
@Component
@RequiredArgsConstructor
public class NotificationEventHandler {

    private final AuthenticatedUserChecker authenticatedUserChecker;

    private final MessageService notificationService;

    @TransactionalEventListener
    public void sendCommentLikePush(CommentLikeEvent event) {

        User user = authenticatedUserChecker.checkAuthenticatedUserExist(event.getUserId());
        notificationService.sendCommentLikeCreatedMessage(user.getEmail(), event.getNickname());
    }

    @TransactionalEventListener
    public void sendCommentPush(CommentEnrollEvent event) {

        User user = authenticatedUserChecker.checkAuthenticatedUserExist(event.getUserId()); // 메모리 작성자
        notificationService.sendCommentCreatedMessage(user.getEmail(), event.getNickname());
    }
}
