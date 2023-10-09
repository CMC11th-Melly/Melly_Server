package cmc.mellyserver.common.event;


import cmc.mellyserver.dbcore.notification.enums.NotificationType;
import cmc.mellyserver.domain.comment.event.CommentEnrollEvent;
import cmc.mellyserver.domain.comment.event.CommentLikeEvent;
import cmc.mellyserver.domain.notification.NotificationService;
import cmc.mellyserver.message.MessageService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

import static cmc.mellyserver.message.constants.NotificationConstants.COMMENT_LIKE_NOTI_CONTENT;


@Slf4j
@Component
@RequiredArgsConstructor
public class NotificationEventHandler {

    private final MessageService pushService;

    private final NotificationService notificationService;


    @Async
    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void sendCommentPush(CommentEnrollEvent event) {

        pushService.sendCommentCreatedMessage(event.getUserId(), event.getMemoryId(), event.getNickname());
        notificationService.createNotification(COMMENT_LIKE_NOTI_CONTENT, NotificationType.COMMENT_ENROLL, event.getUserId(), event.getMemoryId());
    }

    @Async
    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void sendCommentLikePush(CommentLikeEvent event) {

        pushService.sendCommentLikeCreatedMessage(event.getUserId(), event.getMemoryId(), event.getNickname());
        notificationService.createNotification(COMMENT_LIKE_NOTI_CONTENT, NotificationType.COMMENT_LIKE, event.getUserId(), event.getMemoryId());
    }
}