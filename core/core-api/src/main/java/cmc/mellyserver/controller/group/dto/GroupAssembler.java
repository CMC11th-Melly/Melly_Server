package cmc.mellyserver.controller.group.dto;

import cmc.mellyserver.controller.group.dto.request.GroupUpdateRequest;
import cmc.mellyserver.domain.group.dto.request.UpdateGroupRequestDto;

public abstract class GroupAssembler {

    private GroupAssembler() {
    }

    public static UpdateGroupRequestDto updateGroupRequestDto(Long groupId, GroupUpdateRequest groupUpdateRequest) {
        return UpdateGroupRequestDto.builder()
            .groupId(groupId)
            .groupName(groupUpdateRequest.getGroupName())
            .groupType(groupUpdateRequest.getGroupType())
            .groupIcon(groupUpdateRequest.getGroupIcon())
            .build();
    }

}
