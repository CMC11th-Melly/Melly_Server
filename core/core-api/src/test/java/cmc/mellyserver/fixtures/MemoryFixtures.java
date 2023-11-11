package cmc.mellyserver.fixtures;

import java.time.LocalDate;

import cmc.mellyserver.dbcore.memory.Memory;
import cmc.mellyserver.dbcore.memory.OpenType;

public abstract class MemoryFixtures {
	
	public static Memory 메모리(Long placeId, Long userId, Long groupId, String title, OpenType openType) {

		return Memory.builder()
			.placeId(placeId)
			.userId(userId)
			.title(title)
			.groupId(groupId)
			.openType(openType)
			.visitedDate(LocalDate.now())
			.build();
	}
}
