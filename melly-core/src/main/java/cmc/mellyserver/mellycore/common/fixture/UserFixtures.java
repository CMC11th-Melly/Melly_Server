package cmc.mellyserver.mellycore.common.fixture;


import cmc.mellyserver.mellycore.common.enums.Provider;
import cmc.mellyserver.mellycore.common.enums.RoleType;
import cmc.mellyserver.mellycore.common.enums.UserStatus;
import cmc.mellyserver.mellycore.user.application.dto.response.ProfileResponseDto;
import cmc.mellyserver.mellycore.user.domain.User;

public class UserFixtures {


    /* 모카 */
    public static final String 모카_이메일 = "mocha@naver.com";
    public static final String 모카_닉네임 = "모카";
    public static final String 모카_프로필 = "/mocha.png";
    public static final ProfileResponseDto 모카_응답 = new ProfileResponseDto(1L, 모카_닉네임, 모카_이메일, 모카_프로필);


    /* 머식 */
    public static final String 머식_이메일 = "mosik@naver.com";
    public static final String 머식_닉네임 = "머식";
    public static final String 머식_프로필 = "/mosik.png";


    public static User 모카() {

        return User.builder()
                .nickname(모카_닉네임)
                .email(모카_이메일)
                .profileImage(모카_프로필)
                .provider(Provider.DEFAULT)
                .roleType(RoleType.USER)
                .userStatus(UserStatus.ACTIVE)
                .build();
    }

    public static User 머식() {
        return User.builder()
                .nickname(머식_닉네임)
                .email(머식_이메일)
                .profileImage(머식_프로필)
                .provider(Provider.KAKAO)
                .userStatus(UserStatus.ACTIVE)
                .build();
    }
}
