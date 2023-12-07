package fixtures;

import java.time.LocalDate;

import cmc.mellyserver.dbcore.memory.memory.Memory;
import cmc.mellyserver.dbcore.memory.memory.OpenType;

public abstract class MemoryFixtures {

    public static Memory 메모리(Long placeId, Long userId, Long groupId, String title, OpenType openType) {

        return Memory.builder()
            .placeId(placeId)
            .userId(userId)
            .title(title)
            .stars(1L)
            .groupId(groupId)
            .openType(openType)
            .visitedDate(LocalDate.now())
            .build();
    }
}
