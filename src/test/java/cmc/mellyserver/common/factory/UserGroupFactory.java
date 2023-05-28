package cmc.mellyserver.common.factory;

import cmc.mellyserver.common.enums.GroupType;
import cmc.mellyserver.group.domain.UserGroup;
import cmc.mellyserver.user.presentation.dto.response.GroupLoginUserParticipatedResponse;

public class UserGroupFactory {

    public static UserGroup mockUserGroup()
    {
        return new UserGroup(1L,"동기",1,"http://naver.com",2L, GroupType.FRIEND,false);
    }

}
