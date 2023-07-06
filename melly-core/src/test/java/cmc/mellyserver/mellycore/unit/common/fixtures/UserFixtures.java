package cmc.mellyserver.mellycore.unit.common.fixtures;

import cmc.mellyserver.mellycommon.enums.Gender;
import cmc.mellyserver.mellycommon.enums.Provider;
import cmc.mellyserver.mellycore.user.domain.User;
import java.util.List;

public class UserFixtures {

    public static final String 제민_이메일 = "jemin3161@naver.com";
    public static final String 제민_닉네임 = "제민";
    public static final String 재현_이메일 = "jaehyon3161@naver.com";
    public static final String 재현_닉네임 = "재현";

    // 일반 유저
    public static User 제민() {
        return User.builder().nickname(제민_닉네임).email(제민_이메일).provider(Provider.DEFAULT)
                .gender(Gender.MALE).build();
    }

    public static User 재현() {
        return User.builder().nickname(재현_닉네임).email(재현_이메일).provider(Provider.DEFAULT)
                .gender(Gender.MALE).build();
    }

    public static List<User> mockLikeUsersWithId() {
        User user1 = User.builder().uid("test1").email("jemin3161@naver.com")
                .nickname("테스트 유저1")
                .gender(Gender.FEMALE)
                .provider(Provider.GOOGLE).build();

        User user2 = User.builder().uid("test2").email("jemin3162@naver.com")
                .nickname("테스트 유저2")
                .gender(Gender.FEMALE)
                .provider(Provider.NAVER).build();

        User user3 = User.builder().uid("test3").email("jemin3163@naver.com")
                .nickname("테스트 유저3").gender(Gender.MALE)
                .provider(Provider.DEFAULT).build();

        User user4 = User.builder().uid("test4").email("jemin3164@naver.com")
                .nickname("테스트 유저4").gender(Gender.MALE)
                .provider(Provider.DEFAULT).build();

        User user5 = User.builder().uid("test5").email("jemin3165@naver.com")
                .nickname("테스트 유저5")
                .gender(Gender.FEMALE)
                .provider(Provider.KAKAO).build();

        return List.of(
                user1, user2, user3, user4, user5
        );
    }
}
