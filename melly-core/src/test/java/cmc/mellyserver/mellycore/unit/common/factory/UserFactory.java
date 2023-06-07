package cmc.mellyserver.mellycore.unit.common.factory;

import cmc.mellyserver.mellycore.common.enums.Gender;
import cmc.mellyserver.mellycore.common.enums.Provider;
import cmc.mellyserver.mellycore.user.domain.User;
import java.util.List;

public class UserFactory {

    public static List<User> mockLikeUsersWithId() {
        User user1 = User.builder().uid("test1").email("jemin3161@naver.com").userSeq(1L)
                .nickname("테스트 유저1")
                .gender(Gender.FEMALE)
                .provider(Provider.GOOGLE).build();

        User user2 = User.builder().uid("test2").email("jemin3162@naver.com").userSeq(2L)
                .nickname("테스트 유저2")
                .gender(Gender.FEMALE)
                .provider(Provider.NAVER).build();

        User user3 = User.builder().uid("test3").email("jemin3163@naver.com").userSeq(3L)
                .nickname("테스트 유저3").gender(Gender.MALE)
                .provider(Provider.DEFAULT).build();

        User user4 = User.builder().uid("test4").email("jemin3164@naver.com").userSeq(4L)
                .nickname("테스트 유저4").gender(Gender.MALE)
                .provider(Provider.DEFAULT).build();

        User user5 = User.builder().uid("test5").email("jemin3165@naver.com").userSeq(5L)
                .nickname("테스트 유저5")
                .gender(Gender.FEMALE)
                .provider(Provider.KAKAO).build();

        return List.of(
                user1, user2, user3, user4, user5
        );
    }
}
