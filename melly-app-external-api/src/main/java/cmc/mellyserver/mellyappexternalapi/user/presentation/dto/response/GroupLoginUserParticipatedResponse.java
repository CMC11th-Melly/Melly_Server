package cmc.mellyserver.mellyappexternalapi.user.presentation.dto.response;

import cmc.mellyserver.mellydomain.common.enums.GroupType;
import cmc.mellyserver.mellydomain.user.domain.repository.UserDto;
import java.util.List;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

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
            GroupType groupType,
            String invitationLink) {
        this.groupId = groupId;
        this.groupIcon = groupIcon;
        this.groupName = groupName;
        this.groupType = groupType;
        this.invitationLink = invitationLink;
    }
}
