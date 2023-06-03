package cmc.mellyserver.mellyapi.group.application.dto.request;

import cmc.mellyserver.mellycore.common.enums.GroupType;
import lombok.Builder;
import lombok.Data;

@Data
public class UpdateGroupRequestDto {

	private Long groupId;

	private String groupName;

	private GroupType groupType;

	private int groupIcon;

	@Builder
	public UpdateGroupRequestDto(Long groupId, String groupName, GroupType groupType, int groupIcon) {
		this.groupId = groupId;
		this.groupName = groupName;
		this.groupType = groupType;
		this.groupIcon = groupIcon;
	}

}
