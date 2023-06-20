package cmc.mellyserver.mellydomain.group.domain.repository.dto;

import cmc.mellyserver.mellydomain.common.enums.GroupType;
import cmc.mellyserver.mellydomain.user.domain.repository.UserDto;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

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
