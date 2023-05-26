package cmc.mellyserver.memory.presentation.dto.common;

import cmc.mellyserver.common.enums.GroupType;
import lombok.AccessLevel;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class GetUserMemoryCond {

    private GroupType groupType;
}
