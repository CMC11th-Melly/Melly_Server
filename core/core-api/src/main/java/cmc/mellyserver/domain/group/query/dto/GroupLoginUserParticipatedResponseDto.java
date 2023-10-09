package cmc.mellyserver.dbcore.group.query.dto;

import cmc.mellyserver.dbcore.group.GroupAndUser;
import cmc.mellyserver.dbcore.group.enums.GroupType;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class GroupLoginUserParticipatedResponseDto {

    private Long groupId;
    private int groupIcon;
    private String groupName;
    private GroupType groupType;


    @Builder
    public GroupLoginUserParticipatedResponseDto(Long groupId, int groupIcon, String groupName, GroupType groupType) {
        this.groupId = groupId;
        this.groupIcon = groupIcon;
        this.groupName = groupName;
        this.groupType = groupType;
    }

    public static GroupLoginUserParticipatedResponseDto of(GroupAndUser groupAndUser) {
        return GroupLoginUserParticipatedResponseDto.builder()
                .groupId(groupAndUser.getGroup().getId())
                .groupIcon(groupAndUser.getGroup().getGroupIcon())
                .groupName(groupAndUser.getGroup().getGroupName())
                .groupType(groupAndUser.getGroup().getGroupType())
                .build();
    }
}
