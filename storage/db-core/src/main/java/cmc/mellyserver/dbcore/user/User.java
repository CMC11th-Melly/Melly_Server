package cmc.mellyserver.dbcore.user;

import cmc.mellyserver.dbcore.config.jpa.JpaBaseEntity;
import cmc.mellyserver.dbcore.user.enums.AgeGroup;
import cmc.mellyserver.dbcore.user.enums.Gender;
import cmc.mellyserver.dbcore.user.enums.Provider;
import cmc.mellyserver.dbcore.user.enums.RoleType;
import cmc.mellyserver.dbcore.user.enums.UserStatus;
import jakarta.persistence.Column;
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
  @Column(name = "provider")
  private Provider provider;

  @Column(name = "email")
  private String email;

  @Column(name = "password")
  private String password;

  @Column(name = "nickname")
  private String nickname;

  @Column(name = "profile_image")
  private String profileImage;

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

  @Column(name = "fcm_token")
  private String fcmToken;

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

  public void removeProfileImage() {
	this.profileImage = null;
  }

  public void block() {
	this.userStatus = UserStatus.BLOCK;
  }

  public void remove() {
	this.userStatus = UserStatus.DELETE;
  }

  public void initPassword(String password) {
	this.password = password;
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
	  boolean enableAppPush, boolean enableCommentLikePush, boolean enableCommentPush, String fcmToken) {
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
	this.fcmToken = fcmToken;
  }

}
