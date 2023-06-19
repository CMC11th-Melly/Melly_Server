package cmc.mellyserver.mellyappexternalapi.group.application;

import cmc.mellyserver.mellyappexternalapi.group.application.dto.request.CreateGroupRequestDto;
import cmc.mellyserver.mellyappexternalapi.group.application.dto.request.UpdateGroupRequestDto;
import cmc.mellyserver.mellydomain.group.domain.UserGroup;

public interface GroupService {

    UserGroup findGroupById(Long groupId);

    UserGroup saveGroup(CreateGroupRequestDto createGroupRequestDto);

    void participateToGroup(Long loginUserSeq, Long groupId);

    void updateGroup(Long loginUserSeq, UpdateGroupRequestDto updateGroupRequestDto);

    void removeGroup(Long groupOwnerUserSeq, Long groupId);


}
