package cmc.mellyserver.common.factory;

import cmc.mellyserver.common.enums.*;
import cmc.mellyserver.user.domain.User;

import java.util.UUID;

public class UserFactory {

    public static User createEmailLoginUser()
    {
        return User.builder()
                .uid(UUID.randomUUID().toString())
                .email("melly@gmail.com")
                .password("asdfasdf")
                .roleType(RoleType.USER)
                .gender(Gender.MALE)
                .provider(Provider.DEFAULT)
                .ageGroup(AgeGroup.TWO)
                .profileImage("cmc11th.jpg")
                .nickname("떡잎마을방범대")
                .build();

    }
}
