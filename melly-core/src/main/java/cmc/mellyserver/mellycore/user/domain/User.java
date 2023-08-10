package cmc.mellyserver.mellycore.user.domain;

import cmc.mellyserver.mellycore.common.util.jpa.JpaBaseEntity;
import cmc.mellyserver.mellycore.user.domain.enums.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.Objects;
import java.util.UUID;


@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Table(name = "tb_user")
public class User extends JpaBaseEntity {

    private static String NO_PASSWORD = "NO_PASSWORD";

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id", nullable = false)
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
    private Boolean enableAppPush;

    @Column(name = "enable_comment_like_push")
    private Boolean enableCommentLikePush;

    @Column(name = "enable_comment_push")
    private Boolean enableCommentPush;

    @Enumerated(EnumType.STRING)
    @Column(name = "user_status")
    private UserStatus userStatus;

    @Enumerated(EnumType.STRING)
    @Column(name = "password_expired")
    private PasswordExpired passwordExpired;

    @Column(name = "password_init_date")
    private LocalDateTime pwInitDateTime;

    @Column(name = "last_login_time")
    private LocalDateTime lastLoginDateTime;


    public void addSurveyData(final RecommendGroup recommendGroup, final RecommendPlace recommendPlace,
                              final RecommendActivity recommendActivity) {
        this.recommend = new Recommend(recommendGroup, recommendPlace, recommendActivity);
    }

    public void updateProfile(final String nickname, final Gender gender, final AgeGroup ageGroup) {
        this.nickname = nickname;
        this.gender = gender;
        this.ageGroup = ageGroup;
    }

    public void updateOauthInfo(final String nickname, final String socialId) {
        this.nickname = nickname;
        this.socialId = socialId;
    }

    public void changePassword(String password) {
        this.password = password;
    }

    public void changeProfileImage(String image) {
        this.profileImage = image;
    }

    public void changePwInitDate(LocalDateTime now) {
        this.pwInitDateTime = now;
    }

    public void updateLastLoginTime(LocalDateTime now) {
        this.lastLoginDateTime = now;
    }

    public void block() {
        this.userStatus = UserStatus.BLOCK;
    }

    public void remove() {
        this.userStatus = UserStatus.DELETE;
    }

    public void inActive() {
        this.userStatus = UserStatus.INACTIVE;
    }

    public void changeAppPushStatus(boolean enableAppPush) {
        this.enableAppPush = enableAppPush;
    }

    public void changeCommentLikePushStatus(boolean enableCommentLikePush) {
        this.enableCommentLikePush = enableCommentLikePush;
    }

    public void changeCommenPushStatus(boolean enableCommentPush) {
        this.enableCommentPush = enableCommentPush;
    }


    public User setPwChangeStatus() {
        this.passwordExpired = PasswordExpired.Y;
        return this;
    }

    public User changePwExpireStatus() {
        this.passwordExpired = PasswordExpired.Y;
        return this;
    }

    private boolean isDefaultEmailUser() {
        return !Objects.isNull(this.provider) && this.provider.equals(Provider.DEFAULT);
    }

    @PrePersist
    private void init() {

        this.userStatus = UserStatus.ACTIVE;
        this.enableAppPush = true;
        this.enableCommentPush = true;
        this.enableCommentLikePush = true;

        if (isDefaultEmailUser()) {

            this.pwInitDateTime = LocalDateTime.now();
            this.passwordExpired = PasswordExpired.N;
        }
    }

    @Builder
    private User(Long id, String email, String password, RoleType roleType, String profileImage, AgeGroup ageGroup, Gender gender, UserStatus userStatus,
                 String socialId, Provider provider, String nickname, boolean enableAppPush, PasswordExpired passwordExpired, LocalDateTime pwInitDateTime,
                 boolean enableCommentLikePush, boolean enableCommentPush) {

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
        this.passwordExpired = passwordExpired;
        this.pwInitDateTime = pwInitDateTime;
    }

    public static User createEmailLoginUser(String email, String password, String nickname, AgeGroup ageGroup, Gender gender) {

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
    public static User createOauthLoginUser(String socialId, Provider provider, String email, String nickname, AgeGroup ageGroup, Gender gender) {

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
}
