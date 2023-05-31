package cmc.mellyserver.memory.application.dto.response;

import cmc.mellyserver.common.enums.GroupType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@AllArgsConstructor
public class GroupListForSaveMemoryResponseDto {
    private Long groupId;
    private String groupName;
    private GroupType groupType;
}
