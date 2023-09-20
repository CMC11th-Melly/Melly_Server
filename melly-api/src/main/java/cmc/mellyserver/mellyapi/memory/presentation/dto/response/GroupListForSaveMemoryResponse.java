package cmc.mellyserver.mellyapi.memory.presentation.dto.response;

import cmc.mellyserver.mellycore.group.domain.repository.dto.GroupLoginUserParticipatedResponseDto;
import cmc.mellyserver.mellycore.group.domain.enums.GroupType;
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

    public static GroupListForSaveMemoryResponse of(GroupLoginUserParticipatedResponseDto groupLoginUserParticipatedResponseDto) {
        return GroupListForSaveMemoryResponse.builder().groupId(groupLoginUserParticipatedResponseDto.getGroupId()).groupName(groupLoginUserParticipatedResponseDto.getGroupName())
                .groupType(groupLoginUserParticipatedResponseDto.getGroupType()).build();
    }
}
