package cmc.mellyserver.mellycore.user.domain;

import cmc.mellyserver.mellycommon.enums.*;
import cmc.mellyserver.mellycore.common.util.jpa.JpaBaseEntity;
import cmc.mellyserver.mellycore.user.domain.vo.Recommend;
import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.Objects;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@AllArgsConstructor
@Table(name = "tb_user")
public class User extends JpaBaseEntity {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_seq")
    private Long userSeq;

    @Column(name = "user_id", nullable = false)
    private String userId;

    @Column(name = "email")
    private String email;

    @Column(name = "password")
    private String password;

    @Column(name = "nickname", nullable = false)
    private String nickname;

    @Column(name = "profile_iamge")
    private String profileImage;

    @Embedded
    private Recommend recommend;

    @Column(name = "store_capacity")
    private Double storeCapacity;

    @Column(name = "fcm_token")
    private String fcmToken;

    @Column(name = "enable_app_push", nullable = false)
    private boolean enableAppPush;

    @Column(name = "enable_comment_like", nullable = false)
    private boolean enableCommentLike;

    @Column(name = "enable_comment", nullable = false)
    private boolean enableComment;

    @Column(name = "password_init_date")
    private LocalDateTime passwordInitDate;

    @Column(name = "last_login_time")
    private LocalDateTime lastLoginTime;

    @Enumerated(EnumType.STRING)
    @Column(name = "gender", nullable = false)
    private Gender gender;

    @Enumerated(EnumType.STRING)
    @Column(name = "age_group", nullable = false)
    private AgeGroup ageGroup;

    @Enumerated(EnumType.STRING)
    @Column(name = "provider", nullable = false)
    private Provider provider;

    @Enumerated(EnumType.STRING)
    @Column(name = "role_type", nullable = false)
    private RoleType roleType;

    @Enumerated(EnumType.STRING)
    @Column(name = "user_status", nullable = false)
    private UserStatus userStatus;

    @Enumerated(EnumType.STRING)
    private PasswordExpired passwordExpired;

    public User(String uid, Provider provider, RoleType roleType, String password) {
        this.userId = uid;
        this.provider = provider;
        this.roleType = roleType;
        this.password = password;
    }

    @Builder
    private User(Long userSeq, String email, String password, RoleType roleType, String profileImage,
                 AgeGroup ageGroup, Gender gender, UserStatus userStatus,
                 String fcmToken, String uid, Provider provider, String nickname, boolean enableAppPush,
                 PasswordExpired passwordExpired, LocalDateTime passwordInitDate,
                 boolean enableCommentLike, boolean enableComment) {


        this.userSeq = userSeq;
        this.email = email;
        this.password = password;
        this.roleType = roleType;
        this.profileImage = profileImage;
        this.ageGroup = ageGroup;
        this.userStatus = userStatus;
        this.gender = gender;
        this.fcmToken = fcmToken;
        this.userId = uid;
        this.provider = provider;
        this.nickname = nickname;
        this.enableAppPush = enableAppPush;
        this.enableComment = enableComment;
        this.enableCommentLike = enableCommentLike;
        this.passwordExpired = passwordExpired;
        this.passwordInitDate = passwordInitDate;
    }

    public void updateUser(String nickname, Gender gender, String profileImage, AgeGroup ageGroup,
                           boolean enableAppPush, boolean enableCommentLike, boolean enableComment) {
        this.nickname = nickname;
        this.gender = gender;
        this.profileImage = profileImage;
        this.ageGroup = ageGroup;
        this.enableAppPush = enableAppPush;
        this.enableComment = enableComment;
        this.enableCommentLike = enableCommentLike;
    }

    public void remove() {
        this.userStatus = UserStatus.DELETE;
    }

    public User setInactive() {
        this.userStatus = UserStatus.INACTIVE;
        return this;
    }

    public void setFcmToken(String fcmToken) {
        this.fcmToken = fcmToken;
    }

    public void addSurveyData(RecommendGroup recommendGroup, RecommendPlace recommendPlace,
                              RecommendActivity recommendActivity) {
        this.recommend = new Recommend(recommendGroup, recommendPlace, recommendActivity);
    }

    public void updateProfile(String nickname, Gender gender, AgeGroup ageGroup) {
        this.nickname = nickname;
        this.gender = gender;
        this.ageGroup = ageGroup;
    }

    public void chnageProfileImage(String image) {
        this.profileImage = image;
    }

    @PrePersist
    private void init() {
        this.userStatus = UserStatus.ACTIVE;
        if (isDefaultEmailUser()) {
            this.passwordInitDate = LocalDateTime.now();
            this.passwordExpired = PasswordExpired.N;
        }
    }

    public void updateLastLoginTime(LocalDateTime now) {
        this.lastLoginTime = now;
    }

    public void setEnableAppPush(boolean enableAppPush) {
        this.enableAppPush = enableAppPush;
    }

    public void setEnableCommentLike(boolean enableCommentLike) {
        this.enableCommentLike = enableCommentLike;
    }

    public void setEnableComment(boolean enableComment) {
        this.enableComment = enableComment;
    }

    public User setPwChangeStatusAndUpdateLastChangedDate(LocalDateTime localDateTime) {
        this.passwordExpired = PasswordExpired.Y;
        this.passwordInitDate = localDateTime;
        return this;
    }


    private boolean isDefaultEmailUser() {
        return !Objects.isNull(this.provider) && this.provider.equals(Provider.DEFAULT);
    }
}
