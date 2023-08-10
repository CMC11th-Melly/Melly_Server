package cmc.mellyserver.mellycore.common.handler;

import cmc.mellyserver.mellycore.comment.application.event.CommentEnrollEvent;
import cmc.mellyserver.mellycore.comment.application.event.CommentLikeEvent;
import cmc.mellyserver.mellycore.notification.application.MessageService;
import cmc.mellyserver.mellycore.notification.application.NotificationService;
import cmc.mellyserver.mellycore.notification.domain.enums.NotificationType;
import cmc.mellyserver.mellycore.user.domain.User;
import cmc.mellyserver.mellycore.user.domain.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

@Slf4j
@Component
@RequiredArgsConstructor
public class NotificationEventHandler {

    private final UserRepository userRepository;

    private final MessageService pushService;

    private final NotificationService notificationService;

    @Async
    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void sendCommentLikePush(CommentLikeEvent event) {

        User user = userRepository.getById(event.getUserId());

        pushService.sendCommentLikeCreatedMessage(user.getEmail(), event.getNickname());
        notificationService.createNotification("제목", "컨텐츠", NotificationType.COMMENT, user.getId(), 1L);
    }

    @Async
    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void sendCommentPush(CommentEnrollEvent event) {

        User user = userRepository.getById(event.getUserId());

        pushService.sendCommentCreatedMessage(user.getEmail(), event.getNickname());
        notificationService.createNotification("제목", "컨텐츠", NotificationType.COMMENT, user.getId(), 1L);
    }
}
