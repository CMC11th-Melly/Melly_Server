package cmc.mellyserver.dbcore.user;

import java.util.UUID;

import cmc.mellyserver.dbcore.config.jpa.JpaBaseEntity;
import cmc.mellyserver.dbcore.user.enums.AgeGroup;
import cmc.mellyserver.dbcore.user.enums.Gender;
import cmc.mellyserver.dbcore.user.enums.Provider;
import cmc.mellyserver.dbcore.user.enums.RoleType;
import cmc.mellyserver.dbcore.user.enums.UserStatus;
import jakarta.persistence.Column;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "tb_user")
@Entity
public class User extends JpaBaseEntity {

	private static String NO_PASSWORD = "NO_PASSWORD";

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "user_id")
	private Long id;

	@Column(name = "social_id")
	private String socialId;

	@Enumerated(EnumType.STRING)
	@Column(name = "provider", nullable = false)
	private Provider provider;

	@Column(name = "email")
	private String email;

	@Column(name = "password")
	private String password;

	@Column(name = "nickname", nullable = false)
	private String nickname;

	@Column(name = "profile_image")
	private String profileImage;

	@Embedded
	private Recommend recommend;

	@Enumerated(EnumType.STRING)
	@Column(name = "gender")
	private Gender gender;

	@Enumerated(EnumType.STRING)
	@Column(name = "age_group")
	private AgeGroup ageGroup;

	@Enumerated(EnumType.STRING)
	@Column(name = "role_type")
	private RoleType roleType;

	@Column(name = "enable_app_push")
	private boolean enableAppPush;

	@Column(name = "enable_comment_like_push")
	private boolean enableCommentLikePush;

	@Column(name = "enable_comment_push")
	private boolean enableCommentPush;

	@Enumerated(EnumType.STRING)
	@Column(name = "user_status")
	private UserStatus userStatus;

	public void updateProfile(final String nickname, final Gender gender, final AgeGroup ageGroup) {
		this.nickname = nickname;
		this.gender = gender;
		this.ageGroup = ageGroup;
	}

	public void changePassword(String password) {
		this.password = password;
	}

	public void changeProfileImage(String image) {
		this.profileImage = image;
	}

	public void block() {
		this.userStatus = UserStatus.BLOCK;
	}

	public void remove() {
		this.userStatus = UserStatus.DELETE;
	}

	public void changeAppPushStatus(boolean enableAppPush) {
		this.enableAppPush = enableAppPush;
	}

	public void changeCommentLikePushStatus(boolean enableCommentLikePush) {
		this.enableCommentLikePush = enableCommentLikePush;
	}

	public void changeCommentPushStatus(boolean enableCommentPush) {
		this.enableCommentPush = enableCommentPush;
	}

	@PrePersist
	private void init() {

		this.userStatus = UserStatus.ACTIVE;
		this.enableAppPush = true;
		this.enableCommentPush = true;
		this.enableCommentLikePush = true;
	}

	@Builder
	private User(Long id, String email, String password, RoleType roleType, String profileImage, AgeGroup ageGroup,
		Gender gender, UserStatus userStatus, String socialId, Provider provider, String nickname,
		boolean enableAppPush, boolean enableCommentLikePush, boolean enableCommentPush) {
		this.id = id;
		this.email = email;
		this.password = password;
		this.roleType = roleType;
		this.profileImage = profileImage;
		this.ageGroup = ageGroup;
		this.userStatus = userStatus;
		this.gender = gender;
		this.socialId = socialId;
		this.provider = provider;
		this.nickname = nickname;
		this.enableAppPush = enableAppPush;
		this.enableCommentPush = enableCommentPush;
		this.enableCommentLikePush = enableCommentLikePush;
	}

	public static User createEmailLoginUser(String email, String password, String nickname, AgeGroup ageGroup,
		Gender gender) {

		return User.builder()
			.email(email) // 이메일
			.password(password) // 비밀번호
			.nickname(nickname) // 닉네임
			.socialId(UUID.randomUUID().toString())
			.ageGroup(ageGroup)
			.gender(gender)
			.provider(Provider.DEFAULT)
			.roleType(RoleType.USER)
			.build();
	}

	// 처음 로그인 시에 얻어올 수 있는 정보는 OAuth에서는 없다.
	public static User oauthUser(String socialId, Provider provider, String email, String nickname, AgeGroup ageGroup,
		Gender gender) {

		return User.builder()
			.socialId(socialId)
			.provider(provider)
			.email(email)
			.nickname(nickname)
			.ageGroup(ageGroup)
			.gender(gender)
			.password(NO_PASSWORD)
			.roleType(RoleType.USER)
			.build();
	}

	public void removeProfileImage() {
		this.profileImage = null;
	}
}
