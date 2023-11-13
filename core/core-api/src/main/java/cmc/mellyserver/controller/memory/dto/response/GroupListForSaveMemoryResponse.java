package cmc.mellyserver.controller.memory.dto.response;

import cmc.mellyserver.dbcore.group.GroupType;
import cmc.mellyserver.domain.group.query.dto.GroupLoginUserParticipatedResponseDto;
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

  public static GroupListForSaveMemoryResponse of(
	  GroupLoginUserParticipatedResponseDto groupLoginUserParticipatedResponseDto) {
	return GroupListForSaveMemoryResponse.builder()
		.groupId(groupLoginUserParticipatedResponseDto.getGroupId())
		.groupName(groupLoginUserParticipatedResponseDto.getGroupName())
		.groupType(groupLoginUserParticipatedResponseDto.getGroupType())
		.build();
  }

}
