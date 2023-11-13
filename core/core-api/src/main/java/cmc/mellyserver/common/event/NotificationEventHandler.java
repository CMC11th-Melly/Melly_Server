package cmc.mellyserver.common.event;

import static cmc.mellyserver.notification.constants.NotificationConstants.*;

import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

import cmc.mellyserver.dbcore.memory.Memory;
import cmc.mellyserver.dbcore.notification.enums.NotificationType;
import cmc.mellyserver.dbcore.user.User;
import cmc.mellyserver.domain.comment.event.CommentEnrollEvent;
import cmc.mellyserver.domain.comment.event.CommentLikeEvent;
import cmc.mellyserver.domain.memory.MemoryReader;
import cmc.mellyserver.domain.user.UserReader;
import cmc.mellyserver.notification.NotificationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
@RequiredArgsConstructor
public class NotificationEventHandler {

  private final NotificationService pushService;

  private final cmc.mellyserver.domain.notification.NotificationService notificationService;

  private final MemoryReader memoryReader;

  private final UserReader userReader;

  @Async
  @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
  public void sendCommentPush(CommentEnrollEvent event) {

	Memory memory = memoryReader.findById(event.getMemoryId());
	User memoryWriter = userReader.findById(memory.getId());
	pushService.sendCommentCreatedMessage(memory.getId(), memoryWriter.getNickname());
	notificationService.createNotification(COMMENT_LIKE_NOTI_CONTENT, NotificationType.COMMENT_ENROLL,
		memory.getUserId(), event.getMemoryId());
  }

  @Async
  @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
  public void sendCommentLikePush(CommentLikeEvent event) {

	pushService.sendCommentLikeCreatedMessage(event.getUserId(), event.getMemoryId(), event.getNickname());
	notificationService.createNotification(COMMENT_LIKE_NOTI_CONTENT, NotificationType.COMMENT_LIKE,
		event.getUserId(), event.getMemoryId());
  }

}
