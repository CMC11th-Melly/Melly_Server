package cmc.mellyserver.mellyapi.group.presentation.dto.request;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import cmc.mellyserver.mellycore.common.enums.GroupType;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Data
public class GroupCreateRequest {

	@NotNull
	@Size(max = 23, message = "그룹명은 23자 이하입니다.")
	private String groupName;

	@NotNull
	private GroupType groupType;

	private int groupIcon;

	@Builder
	public GroupCreateRequest(String groupName, GroupType groupType, int groupIcon) {
		this.groupName = groupName;
		this.groupType = groupType;
		this.groupIcon = groupIcon;
	}
}
