package cmc.mellyserver.user.presentation.dto.response;

import cmc.mellyserver.common.enums.GroupType;
import cmc.mellyserver.user.presentation.dto.common.UserDto;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
public class GroupLoginUserParticipatedResponse {

    private Long groupId;
    private int groupIcon;
    private String groupName;
    private List<UserDto> users;
    private GroupType groupType;
    private String invitationLink;

    @Builder
    public GroupLoginUserParticipatedResponse(Long groupId, int groupIcon, String groupName, GroupType groupType, String invitationLink) {
        this.groupId = groupId;
        this.groupIcon = groupIcon;
        this.groupName = groupName;
        this.groupType = groupType;
        this.invitationLink = invitationLink;
    }
}
