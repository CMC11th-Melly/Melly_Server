package cmc.mellyserver.mellyapi.group.application;

import cmc.mellyserver.mellyapi.group.application.dto.request.CreateGroupRequestDto;
import cmc.mellyserver.mellyapi.group.application.dto.request.UpdateGroupRequestDto;
import cmc.mellyserver.mellycore.group.domain.UserGroup;

public interface GroupService {

	UserGroup findGroupById(Long groupId);

	UserGroup saveGroup(CreateGroupRequestDto createGroupRequestDto);

	void participateToGroup(Long loginUserSeq, Long groupId);

	void updateGroup(UpdateGroupRequestDto updateGroupRequestDto);

	void removeGroup(Long groupOwnerUserSeq, Long groupId);
}
