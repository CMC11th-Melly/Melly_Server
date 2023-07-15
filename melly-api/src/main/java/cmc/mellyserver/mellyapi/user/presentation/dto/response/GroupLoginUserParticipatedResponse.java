package cmc.mellyserver.mellyapi.user.presentation.dto.response;

import cmc.mellyserver.mellycommon.enums.GroupType;
import cmc.mellyserver.mellycore.user.domain.repository.UserDto;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
public class GroupLoginUserParticipatedResponse {

    private Long groupId;
    private int groupIcon;
    private String groupName;
    private List<UserDto> users;
    private GroupType groupType;
    private String invitationLink;

    @Builder
    public GroupLoginUserParticipatedResponse(Long groupId, int groupIcon, String groupName,
                                              GroupType groupType, List<UserDto> users,
                                              String invitationLink) {
        this.groupId = groupId;
        this.groupIcon = groupIcon;
        this.users = users;
        this.groupName = groupName;
        this.groupType = groupType;
        this.invitationLink = invitationLink;
    }
}