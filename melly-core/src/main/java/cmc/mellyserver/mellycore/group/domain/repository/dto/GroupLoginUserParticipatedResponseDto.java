package cmc.mellyserver.mellycore.group.domain.repository.dto;

import cmc.mellyserver.mellycore.common.enums.GroupType;
import cmc.mellyserver.mellycore.user.domain.repository.UserDto;
import java.util.List;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class GroupLoginUserParticipatedResponseDto {

    private Long groupId;
    private int groupIcon;
    private String groupName;
    private List<UserDto> users;
    private GroupType groupType;
    private String invitationLink;

    public GroupLoginUserParticipatedResponseDto(Long groupId, int groupIcon, String groupName,
            GroupType groupType,
            String invitationLink) {
        this.groupId = groupId;
        this.groupIcon = groupIcon;
        this.groupName = groupName;
        this.groupType = groupType;
        this.invitationLink = invitationLink;
    }

    public GroupLoginUserParticipatedResponseDto(Long groupId, int groupIcon, String groupName,
            List<UserDto> users,
            GroupType groupType, String invitationLink) {
        this.groupId = groupId;
        this.groupIcon = groupIcon;
        this.groupName = groupName;
        this.users = users;
        this.groupType = groupType;
        this.invitationLink = invitationLink;
    }
}
