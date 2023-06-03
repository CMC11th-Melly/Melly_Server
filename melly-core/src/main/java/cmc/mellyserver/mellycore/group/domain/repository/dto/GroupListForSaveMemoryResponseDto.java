package cmc.mellyserver.mellycore.group.domain.repository.dto;

import cmc.mellyserver.mellycore.common.enums.GroupType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@AllArgsConstructor
public class GroupListForSaveMemoryResponseDto {
	private Long groupId;
	private String groupName;
	private GroupType groupType;
}
