package cmc.mellyserver.mellyapi.domain.memory.event;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class GroupUserMemoryCreatedEvent {

    private Long userId;

    private Long memoryId;

    private Long groupId;

}
