package fixtures;

import cmc.mellyserver.dbcore.user.User;
import cmc.mellyserver.dbcore.user.enums.Provider;
import cmc.mellyserver.dbcore.user.enums.RoleType;
import cmc.mellyserver.dbcore.user.enums.UserStatus;

public abstract class UserFixtures {

  /* 모카 */
  public static final String 모카_이메일 = "mocha@naver.com";

  public static final String 모카_닉네임 = "모카";

  public static final String 모카_프로필 = "/mocha.png";

  /* 머식 */
  public static final String 머식_이메일 = "mosik@naver.com";

  public static final String 머식_닉네임 = "머식";

  public static final String 머식_프로필 = "/mosik.png";

  public static final String 금지_이메일 = "geomji@naver.com";

  public static final String 금지_닉네임 = "금지";

  public static final String 금지_프로필 = "/geomji.png";

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

  public static User 금지() {
	return User.builder()
		.nickname(금지_닉네임)
		.email(금지_이메일)
		.profileImage(금지_프로필)
		.provider(Provider.KAKAO)
		.userStatus(UserStatus.ACTIVE)
		.build();
  }
}
