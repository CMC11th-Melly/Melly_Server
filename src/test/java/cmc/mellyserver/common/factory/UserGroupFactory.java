package cmc.mellyserver.common.factory;

import cmc.mellyserver.common.enums.GroupType;
import cmc.mellyserver.group.application.dto.request.CreateGroupRequestDto;
import cmc.mellyserver.group.application.dto.request.UpdateGroupRequestDto;
import cmc.mellyserver.group.domain.UserGroup;
import cmc.mellyserver.group.presentation.dto.request.GroupCreateRequest;
import cmc.mellyserver.group.presentation.dto.request.GroupUpdateRequest;
import cmc.mellyserver.user.presentation.dto.response.GroupLoginUserParticipatedResponse;

public class UserGroupFactory {

    public static UserGroup mockUserGroup()
    {
        return new UserGroup(1L,"동기",1,"http://naver.com",2L, GroupType.FRIEND,false);
    }

    public static GroupCreateRequest mockGroupCreateRequest()
    {
        return GroupCreateRequest.builder()
                .groupName("테스트 그룹")
                .groupIcon(1)
                .groupType(GroupType.FRIEND)
                .build();
    }

    public static GroupUpdateRequest mockGroupUpdateRequest()
    {
        return GroupUpdateRequest.builder()
                .groupName("테스트 그룹")
                .groupIcon(1)
                .groupType(GroupType.FRIEND)
                .build();
    }

    public static CreateGroupRequestDto mockCreateGroupRequestDto()
    {
        return CreateGroupRequestDto.builder()
                .userSeq(2L)
                .groupIcon(1)
                .groupName("테스트 그룹")
                .groupType(GroupType.FRIEND)
                .build();
    }

    public static UpdateGroupRequestDto mockUpdateGroupRequestDto()
    {
        return UpdateGroupRequestDto.builder()
                .groupId(1L)
                .groupIcon(2)
                .groupName("수정된 그룹 이름")
                .groupType(GroupType.COMPANY)
                .build();
    }


}
