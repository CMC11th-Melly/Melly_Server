package cmc.mellyserver.common.event.handler;

import static cmc.mellyserver.config.async.ExecutorConstants.*;
import static cmc.mellyserver.dbcore.notification.enums.NotificationType.*;
import static cmc.mellyserver.notification.constants.AlarmConstants.*;

import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

import cmc.mellyserver.dbcore.memory.memory.Memory;
import cmc.mellyserver.dbcore.user.User;
import cmc.mellyserver.domain.comment.event.CommentCreatedEvent;
import cmc.mellyserver.domain.memory.MemoryReader;
import cmc.mellyserver.domain.notification.NotificationService;
import cmc.mellyserver.domain.user.UserReader;
import cmc.mellyserver.notification.AlarmSender;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class NotificationEventHandler {

    private final MemoryReader memoryReader;

    private final UserReader userReader;

    private final AlarmSender alarmSender;

    private final NotificationService notificationService;

    @Async(NOTIFICATION_BEAN_NAME)
    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void sendCommentCreatedPush(CommentCreatedEvent event) {
        Memory memory = memoryReader.read(event.getMemoryId());
        User commentWriter = userReader.findById(event.getWriterId());
        notificationService.createNotification(COMMENT_CREATED_TITLE, COMMENT_ENROLL, memory.getUserId(),
            event.getMemoryId());
        alarmSender.sendCommentCreatedAlarm(commentWriter.getNickname(), memory.getUserId());
    }
}
