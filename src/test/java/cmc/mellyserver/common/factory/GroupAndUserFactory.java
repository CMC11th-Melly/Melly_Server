package cmc.mellyserver.common.factory;

import cmc.mellyserver.group.domain.GroupAndUser;

public class GroupAndUserFactory {

    public static GroupAndUser mockGroupAndUser()
    {
        return GroupAndUser.builder()
                .user(null)
                .group(null)
                .build();
    }
}
