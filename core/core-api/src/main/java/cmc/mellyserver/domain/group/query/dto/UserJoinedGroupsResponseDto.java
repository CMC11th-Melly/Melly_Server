package cmc.mellyserver.domain.group.query.dto;

import cmc.mellyserver.dbcore.group.GroupType;
import cmc.mellyserver.dbcore.group.UserGroup;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class UserJoinedGroupsResponseDto {

    private Long groupId;

    private int groupIcon;

    private String groupName;

    private GroupType groupType;

    @Builder
    public UserJoinedGroupsResponseDto(Long groupId, int groupIcon, String groupName, GroupType groupType) {
        this.groupId = groupId;
        this.groupIcon = groupIcon;
        this.groupName = groupName;
        this.groupType = groupType;
    }

    public static UserJoinedGroupsResponseDto of(UserGroup userGroup) {
        return UserJoinedGroupsResponseDto.builder()
            .groupId(userGroup.getId())
            .groupIcon(userGroup.getIcon())
            .groupName(userGroup.getName())
            .groupType(userGroup.getGroupType())
            .build();
    }

}
