package cmc.mellyserver.common.event.handler;

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

    private final AlarmSender alarmSender;

    private final NotificationService notificationService;

    private final MemoryReader memoryReader;

    private final UserReader userReader;

    @Async("notificationTaskExecutor")
    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void sendCommentCreatedPush(CommentCreatedEvent event) {

        Memory memory = memoryReader.read(event.getMemoryId());
        User memoryOwner = userReader.findById(memory.getId());
        User commentWriter = userReader.findById(event.getWriterId());
        alarmSender.sendCommentCreatedAlarm(commentWriter.getNickname(), memoryOwner.getId());
        notificationService.createNotification(COMMENT_CREATED_TITLE, COMMENT_ENROLL, memory.getUserId(),
            event.getMemoryId());
    }
}
