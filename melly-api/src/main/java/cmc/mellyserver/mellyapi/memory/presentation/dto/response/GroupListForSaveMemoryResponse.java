package cmc.mellyserver.mellyapi.memory.presentation.dto.response;

import cmc.mellyserver.mellycore.common.enums.GroupType;
import lombok.Builder;
import lombok.Data;

@Data
public class GroupListForSaveMemoryResponse {
	private Long groupId;
	private String groupName;
	private GroupType groupType;

	@Builder
	public GroupListForSaveMemoryResponse(Long groupId, String groupName, GroupType groupType) {
		this.groupId = groupId;
		this.groupName = groupName;
		this.groupType = groupType;
	}
}
