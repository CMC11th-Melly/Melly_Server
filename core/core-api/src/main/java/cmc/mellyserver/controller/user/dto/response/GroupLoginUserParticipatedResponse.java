package cmc.mellyserver.controller.user.dto.response;

import java.util.List;

import cmc.mellyserver.dbcore.group.enums.GroupType;
import cmc.mellyserver.dbcore.user.GroupMemberResponseDto;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class GroupLoginUserParticipatedResponse {

	private Long groupId;

	private int groupIcon;

	private String groupName;

	private List<GroupMemberResponseDto> users;

	private GroupType groupType;

	private String invitationLink;

	@Builder
	public GroupLoginUserParticipatedResponse(Long groupId, int groupIcon, String groupName, GroupType groupType,
		List<GroupMemberResponseDto> users, String invitationLink) {
		this.groupId = groupId;
		this.groupIcon = groupIcon;
		this.users = users;
		this.groupName = groupName;
		this.groupType = groupType;
		this.invitationLink = invitationLink;
	}

}
