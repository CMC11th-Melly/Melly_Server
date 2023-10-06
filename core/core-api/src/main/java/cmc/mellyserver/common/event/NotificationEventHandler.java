package cmc.mellyserver.common.event;


import cmc.mellyserver.dbcore.group.GroupAndUserRepository;
import cmc.mellyserver.dbcore.notification.enums.NotificationType;
import cmc.mellyserver.dbcore.user.User;
import cmc.mellyserver.dbcore.user.UserRepository;
import cmc.mellyserver.domain.comment.event.CommentEnrollEvent;
import cmc.mellyserver.domain.comment.event.CommentLikeEvent;
import cmc.mellyserver.domain.memory.event.GroupUserMemoryCreatedEvent;
import cmc.mellyserver.domain.notification.NotificationService;
import cmc.mellyserver.message.MessageService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

import java.util.List;

import static cmc.mellyserver.message.constants.NotificationConstants.COMMENT_LIKE_NOTI_CONTENT;
import static cmc.mellyserver.message.constants.NotificationConstants.GROUP_USER_CREATED_MEMORY_CONTENT;


@Slf4j
@Component
@RequiredArgsConstructor
public class NotificationEventHandler {

    private final MessageService pushService;

    private final NotificationService notificationService;

    private final GroupAndUserRepository groupAndUserRepository;

    private final UserRepository userRepository;

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

    @Async
    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void sendGroupUserMemoryCreatedPush(GroupUserMemoryCreatedEvent event) {


        List<User> usersParticipatedInGroup = groupAndUserRepository.getUsersParticipatedInGroup(event.getGroupId());
        String nickname = userRepository.getById(event.getUserId()).getNickname();

        usersParticipatedInGroup.stream()
                .filter(user -> excludeWriter(event, user))
                .forEach(user -> {
                    pushService.sendGroupUserCreatedMemoryMessage(event.getUserId(), event.getMemoryId(), nickname);
                    notificationService.createNotification(GROUP_USER_CREATED_MEMORY_CONTENT, NotificationType.GROUP_USER_CREATED_MEMORY, event.getUserId(), event.getMemoryId());
                });
    }

    private boolean excludeWriter(GroupUserMemoryCreatedEvent event, User user) {
        return !user.getId().equals(event.getUserId());
    }
}
