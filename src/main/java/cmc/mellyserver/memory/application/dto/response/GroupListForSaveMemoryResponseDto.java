package cmc.mellyserver.memory.application.dto.response;

import cmc.mellyserver.common.enums.GroupType;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class GroupListForSaveMemoryResponseDto {
    private Long groupId;
    private String groupName;
    private GroupType groupType;
}