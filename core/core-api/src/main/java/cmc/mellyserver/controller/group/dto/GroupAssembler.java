package cmc.mellyserver.controller.group.dto;

import cmc.mellyserver.controller.group.dto.request.GroupCreateRequest;
import cmc.mellyserver.controller.group.dto.request.GroupUpdateRequest;
import cmc.mellyserver.controller.user.dto.response.GroupLoginUserParticipatedResponse;
import cmc.mellyserver.domain.group.dto.request.CreateGroupRequestDto;
import cmc.mellyserver.domain.group.dto.request.UpdateGroupRequestDto;
import cmc.mellyserver.domain.group.query.dto.GroupDetailResponseDto;

public abstract class GroupAssembler {

	private GroupAssembler() {
	}

	public static GroupLoginUserParticipatedResponse getUserGroupResponse(
			GroupDetailResponseDto groupDetailResponseDto) {
		return new GroupLoginUserParticipatedResponse(groupDetailResponseDto.getGroupId(),
				groupDetailResponseDto.getGroupIcon(), groupDetailResponseDto.getGroupName(),
				groupDetailResponseDto.getGroupType(), groupDetailResponseDto.getUsers(),
				groupDetailResponseDto.getInvitationLink());
	}

	public static CreateGroupRequestDto createGroupRequestDto(Long id, GroupCreateRequest groupCreateRequest) {
		return CreateGroupRequestDto.builder()
			.id(id)
			.groupName(groupCreateRequest.getGroupName())
			.groupType(groupCreateRequest.getGroupType())
			.groupIcon(groupCreateRequest.getGroupIcon())
			.build();
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
