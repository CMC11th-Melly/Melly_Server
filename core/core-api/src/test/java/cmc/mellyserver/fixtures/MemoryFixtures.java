package cmc.mellyserver.fixtures;

import java.time.LocalDate;
import java.util.List;

import cmc.mellyserver.dbcore.memory.Memory;
import cmc.mellyserver.dbcore.memory.enums.OpenType;

public abstract class MemoryFixtures {

	private final static List<String> keywords = List.of("기뻐요", "슬퍼요");

	public static Memory 메모리(Long placeId, Long userId, Long groupId, String title, OpenType openType) {

		return Memory.builder()
			.placeId(placeId)
			.userId(userId)
			.title(title)
			.groupId(groupId)
			.openType(openType)
			.keyword(keywords)
			.visitedDate(LocalDate.now())
			.build();
	}
}
