package cmc.mellyserver.common.factory;

import cmc.mellyserver.common.enums.GroupType;
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

}
