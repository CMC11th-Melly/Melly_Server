package cmc.mellyserver.mellyapi.group.presentation.dto;

import cmc.mellyserver.mellyapi.group.application.dto.request.CreateGroupRequestDto;
import cmc.mellyserver.mellyapi.group.application.dto.request.UpdateGroupRequestDto;
import cmc.mellyserver.mellyapi.group.presentation.dto.request.GroupCreateRequest;
import cmc.mellyserver.mellyapi.group.presentation.dto.request.GroupUpdateRequest;
import cmc.mellyserver.mellyapi.user.presentation.dto.response.GroupLoginUserParticipatedResponse;
import cmc.mellyserver.mellycore.group.domain.UserGroup;

public abstract class GroupAssembler {

    private GroupAssembler() {
    }

    public static GroupLoginUserParticipatedResponse getUserGroupResponse(UserGroup userGroup) {
        return new GroupLoginUserParticipatedResponse(userGroup.getId(),
                userGroup.getGroupIcon(),
                userGroup.getGroupName(),
                userGroup.getGroupType(),
                userGroup.getInviteLink()
        );
    }

    public static CreateGroupRequestDto createGroupRequestDto(Long userSeq,
            GroupCreateRequest groupCreateRequest) {
        return CreateGroupRequestDto.builder()
                .userSeq(userSeq)
                .groupName(groupCreateRequest.getGroupName())
                .groupType(groupCreateRequest.getGroupType())
                .groupIcon(groupCreateRequest.getGroupIcon())
                .build();
    }

    public static UpdateGroupRequestDto updateGroupRequestDto(Long groupId,
            GroupUpdateRequest groupUpdateRequest) {
        return UpdateGroupRequestDto.builder()
                .groupId(groupId)
                .groupName(groupUpdateRequest.getGroupName())
                .groupType(groupUpdateRequest.getGroupType())
                .groupIcon(groupUpdateRequest.getGroupIcon())
                .build();
    }
}
