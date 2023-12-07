package cmc.mellyserver.controller.user.dto.response;

import java.util.List;

import cmc.mellyserver.dbcore.group.GroupType;
import cmc.mellyserver.domain.group.dto.GroupMemberResponseDto;
import cmc.mellyserver.domain.group.query.dto.GroupResponseDto;
import lombok.Builder;

public record GroupResponse(
    Long groupId,

    int groupIcon,

    String groupName,

    GroupType groupType,

    List<GroupMemberResponseDto> users
) {
    @Builder
    public GroupResponse {
    }

    public static GroupResponse of(GroupResponseDto groupResponseDto) {
        return GroupResponse.builder()
            .groupId(groupResponseDto.groupId())
            .groupIcon(groupResponseDto.groupIcon())
            .groupName(groupResponseDto.groupName())
            .groupType(groupResponseDto.groupType())
            .users(groupResponseDto.groupMembers())
            .build();
    }
}
