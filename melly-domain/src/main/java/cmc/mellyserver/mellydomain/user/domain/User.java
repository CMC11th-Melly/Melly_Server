package cmc.mellyserver.mellydomain.user.domain;

import cmc.mellyserver.mellydomain.common.enums.*;
import cmc.mellyserver.mellydomain.common.util.jpa.JpaBaseEntity;
import cmc.mellyserver.mellydomain.user.domain.vo.Recommend;
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

    @Column(name = "user_id")
    private String userId;

    @Column(name = "email")
    private String email;

    @Column(name = "password")
    private String password;

    @Column(name = "nickname")
    private String nickname;

    @Column(name = "profile_iamge")
    private String profileImage;

    @Embedded
    private Recommend recommend;

    @Column(name = "store_capacity")
    private Double storeCapacity;

    @Column(name = "fcm_token")
    private String fcmToken;

    private boolean enableAppPush;

    private boolean enableCommentLike;

    private boolean enableComment;

    private LocalDateTime passwordInitDate;

    private LocalDateTime lastLoginTime;

    @Enumerated(EnumType.STRING)
    private Gender gender;

    @Enumerated(EnumType.STRING)
    private AgeGroup ageGroup;

    @Enumerated(EnumType.STRING)
    private Provider provider;

    @Enumerated(EnumType.STRING)
    private RoleType roleType;

    @Enumerated(EnumType.STRING)
    private DeleteStatus isDeleted;

    @Enumerated(EnumType.STRING)
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
                 boolean enableCommentLike, boolean enableComment, DeleteStatus isDeleted) {
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
        this.isDeleted = isDeleted;
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
        this.isDeleted = DeleteStatus.Y;
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
        this.isDeleted = DeleteStatus.N;
        this.userStatus = UserStatus.ACTIVE;
        if (!Objects.isNull(this.provider) && this.provider.equals(Provider.DEFAULT)) {
            this.passwordInitDate = LocalDateTime.now();
            this.passwordExpired = PasswordExpired.N;
        }

    }

    public void removeUser() {
        this.isDeleted = DeleteStatus.Y;
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

    public User setInactive() {
        this.userStatus = UserStatus.INACTIVE;
        return this;
    }

    public User setPwChangeStatusAndUpdateLastChangedDate(LocalDateTime localDateTime) {
        this.passwordExpired = PasswordExpired.Y;
        this.passwordInitDate = localDateTime;
        return this;
    }
}
