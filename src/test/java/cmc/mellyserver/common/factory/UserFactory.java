package cmc.mellyserver.common.factory;

import cmc.mellyserver.auth.presentation.dto.Provider;
import cmc.mellyserver.user.domain.User;
import cmc.mellyserver.user.domain.enums.AgeGroup;
import cmc.mellyserver.user.domain.enums.Gender;
import cmc.mellyserver.user.domain.enums.RoleType;

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
