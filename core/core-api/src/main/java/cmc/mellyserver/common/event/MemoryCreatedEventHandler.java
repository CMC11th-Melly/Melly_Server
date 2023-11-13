package cmc.mellyserver.common.event;

import java.util.List;

import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

import cmc.mellyserver.dbcore.group.GroupAndUserRepository;
import cmc.mellyserver.dbcore.memory.Memory;
import cmc.mellyserver.dbcore.user.User;
import cmc.mellyserver.domain.memory.MemoryReader;
import cmc.mellyserver.notification.NotificationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
@RequiredArgsConstructor
public class MemoryCreatedEventHandler {

  private final MemoryReader memoryReader;
  private final GroupAndUserRepository groupAndUserRepository;
  private final NotificationService notificationService;

  @Async
  @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
  public void messageCreatedEvent(MemoryCreatedEvent event) {

	Memory memory = memoryReader.findById(event.memoryId());
	Long groupId = memory.getGroupId();
	List<User> users = groupAndUserRepository.getUsersParticipatedInGroup(groupId);

  }
}
