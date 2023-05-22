package cmc.mellyserver.memory.presentation.dto.response;

import cmc.mellyserver.common.enums.GroupType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
public class GroupListForSaveMemoryResponse {
    private Long groupId;
    private String groupName;
    private GroupType groupType;

    @Builder
    public GroupListForSaveMemoryResponse(Long groupId, String groupName, GroupType groupType) {
        this.groupId = groupId;
        this.groupName = groupName;
        this.groupType = groupType;
    }
}
