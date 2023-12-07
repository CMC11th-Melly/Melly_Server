package cmc.mellyserver.domain.group.query.dto;

import java.io.Serializable;
import java.util.List;

import cmc.mellyserver.dbcore.group.GroupType;
import cmc.mellyserver.dbcore.group.UserGroup;
import cmc.mellyserver.domain.group.dto.GroupMemberResponseDto;
import lombok.Builder;

public record GroupResponseDto(
    Long groupId,

    int groupIcon,

    String groupName,

    GroupType groupType,

    List<GroupMemberResponseDto> groupMembers
) implements Serializable {

    @Builder
    public GroupResponseDto {
    }

    public static GroupResponseDto of(UserGroup userGroup, List<GroupMemberResponseDto> groupMembers) {
        return GroupResponseDto.builder()
            .groupId(userGroup.getId())
            .groupIcon(userGroup.getIcon())
            .groupType(userGroup.getGroupType())
            .groupName(userGroup.getName())
            .groupMembers(groupMembers)
            .build();
    }
}
