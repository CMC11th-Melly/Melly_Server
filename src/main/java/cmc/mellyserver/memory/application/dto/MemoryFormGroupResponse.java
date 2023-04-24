package cmc.mellyserver.memory.application.dto;

import cmc.mellyserver.common.enums.GroupType;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class MemoryFormGroupResponse {
    private Long groupId;
    private String groupName;
    private GroupType groupType;
}
