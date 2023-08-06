package cmc.mellyserver.mellycore.unit.common.fixtures;

import cmc.mellyserver.mellycommon.enums.AgeGroup;
import cmc.mellyserver.mellycommon.enums.Provider;
import cmc.mellyserver.mellycommon.enums.RoleType;
import cmc.mellyserver.mellycommon.enums.UserStatus;
import cmc.mellyserver.mellycore.user.domain.User;

import static cmc.mellyserver.mellycommon.enums.Gender.FEMALE;
import static cmc.mellyserver.mellycommon.enums.Gender.MALE;

public class UserFixtures {

    public static final String TEST_USER_1_EMAIL = "jemin3161@naver.com";
    public static final String TEST_USER_1_NICKNAME = "제민";
    public static final String TEST_USER_2_EMAIL = "jiwon1316@naver.com";
    public static final String TEST_USER_2_NICKNAME = "지원";

    // 일반 유저
    public static User 테스트_유저_1() {
        return User.builder()
                .nickname(TEST_USER_1_NICKNAME)
                .socialId("socialId")
                .email(TEST_USER_1_EMAIL)
                .provider(Provider.DEFAULT)
                .ageGroup(AgeGroup.THREE)
                .roleType(RoleType.USER)
                .userStatus(UserStatus.ACTIVE)
                .gender(MALE).build();
    }

    public static User 테스트_유저_2() {
        return User.builder()
                .nickname(TEST_USER_2_NICKNAME)
                .socialId("socialId")
                .email(TEST_USER_2_EMAIL)
                .provider(Provider.DEFAULT)
                .ageGroup(AgeGroup.THREE)
                .roleType(RoleType.USER)
                .userStatus(UserStatus.ACTIVE)
                .gender(FEMALE).build();
    }
}
