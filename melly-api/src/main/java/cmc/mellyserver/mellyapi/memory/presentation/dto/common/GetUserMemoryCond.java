package cmc.mellyserver.mellyapi.memory.presentation.dto.common;

import cmc.mellyserver.mellycore.common.enums.GroupType;
import lombok.AccessLevel;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class GetUserMemoryCond {

	private GroupType groupType;
}
