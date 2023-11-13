package cmc.mellyserver.domain.group.query.dto;

import java.io.Serializable;
import java.util.List;

import cmc.mellyserver.dbcore.group.GroupType;
import cmc.mellyserver.dbcore.group.UserGroup;
import cmc.mellyserver.domain.group.dto.GroupMemberResponseDto;
import lombok.Builder;
import lombok.Data;

@Data
public class GroupDetailResponseDto implements Serializable {

	private Long groupId;

	private int groupIcon;

	private String groupName;

	private GroupType groupType;

	private String invitationLink;

	private List<GroupMemberResponseDto> groupMembers;

	@Builder
	public GroupDetailResponseDto(Long groupId, int groupIcon, String groupName, GroupType groupType,
		String invitationLink, List<GroupMemberResponseDto> users) {
		this.groupId = groupId;
		this.groupIcon = groupIcon;
		this.groupName = groupName;
		this.groupMembers = users;
		this.groupType = groupType;
		this.invitationLink = invitationLink;
	}

	public static GroupDetailResponseDto of(UserGroup userGroup, List<GroupMemberResponseDto> groupMembers) {
		return new GroupDetailResponseDto(userGroup.getId(), userGroup.getIcon(), userGroup.getName(),
			userGroup.getGroupType(), userGroup.getInviteLink(), groupMembers);
	}

}
