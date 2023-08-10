package cmc.mellyserver.mellycore.group.domain.repository.dto;

import cmc.mellyserver.mellycore.group.domain.UserGroup;
import cmc.mellyserver.mellycore.group.domain.enums.GroupType;
import cmc.mellyserver.mellycore.user.domain.User;
import cmc.mellyserver.mellycore.user.domain.repository.UserDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.io.Serializable;
import java.util.List;
import java.util.stream.Collectors;

@Data
@AllArgsConstructor
public class GroupDetailResponseDto implements Serializable {

    private Long groupId;

    private int groupIcon;

    private String groupName;

    private List<UserDto> users;

    private GroupType groupType;

    private String invitationLink;


    @Builder
    public GroupDetailResponseDto(Long groupId, int groupIcon, String groupName, GroupType groupType, String invitationLink, List<UserDto> users) {
        this.groupId = groupId;
        this.groupIcon = groupIcon;
        this.groupName = groupName;
        this.users = users;
        this.groupType = groupType;
        this.invitationLink = invitationLink;
    }

    public static GroupDetailResponseDto of(UserGroup userGroup, List<User> userList) {
        return new GroupDetailResponseDto(userGroup.getId(),
                userGroup.getGroupIcon(),
                userGroup.getGroupName(),
                userList.stream().map(user -> new UserDto(user.getId(), user.getProfileImage(), user.getNickname())).collect(Collectors.toList()),
                userGroup.getGroupType(),
                userGroup.getInviteLink());
    }
}
