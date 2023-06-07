package cmc.mellyserver.mellycore.user.domain;

import cmc.mellyserver.mellycore.common.enums.AgeGroup;
import cmc.mellyserver.mellycore.common.enums.DeleteStatus;
import cmc.mellyserver.mellycore.common.enums.Gender;
import cmc.mellyserver.mellycore.common.enums.Provider;
import cmc.mellyserver.mellycore.common.enums.RecommendActivity;
import cmc.mellyserver.mellycore.common.enums.RecommendGroup;
import cmc.mellyserver.mellycore.common.enums.RecommendPlace;
import cmc.mellyserver.mellycore.common.enums.RoleType;
import cmc.mellyserver.mellycore.common.util.jpa.JpaBaseEntity;
import java.time.LocalDateTime;
import java.util.Objects;
import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.PrePersist;
import javax.persistence.Table;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

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

    private String email;

    private String password;

    private String nickname;

    private String profileImage;

    @Embedded
    private Recommend recommend;

    @Column(name = "store_capacity")
    private Double storeCapacity;

    private String fcmToken;

    private boolean enableAppPush;

    private boolean enableCommentLike;

    private boolean enableComment;

    private LocalDateTime passwordInitDate;

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
    public User(Long userSeq, String email, String password, RoleType roleType, String profileImage,
            AgeGroup ageGroup, Gender gender, UserStatus userStatus,
            String fcmToken, String uid, Provider provider, String nickname, boolean enableAppPush,
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
