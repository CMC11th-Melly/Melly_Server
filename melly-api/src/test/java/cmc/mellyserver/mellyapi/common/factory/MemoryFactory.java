package cmc.mellyserver.mellyapi.common.factory;

import java.time.LocalDateTime;

import cmc.mellyserver.mellycommon.enums.GroupType;
import cmc.mellyserver.mellycommon.enums.OpenType;
import cmc.mellyserver.mellycore.memory.domain.Memory;
import cmc.mellyserver.mellycore.memory.domain.vo.GroupInfo;

public class MemoryFactory {

	public static Memory mockMemory() {
		return Memory.builder()
			.title("테스트 제목")
			.content("테스트 컨텐츠")
			.userId(1L)
			.groupInfo(new GroupInfo("테스트 그룹", GroupType.FRIEND, 1L))
			.openType(OpenType.ALL)
			.stars(4L)
			.visitedDate(LocalDateTime.of(2023, 5, 29, 10, 20))
			.build();
	}

}