package cmc.mellyserver.mellyapi.group.presentation.dto.response;

import cmc.mellyserver.mellycore.common.enums.GroupType;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class GroupCreateResponse {

	private Long groupId;
	private String groupName;
	private GroupType groupType;
	private int groupIcon;
}
