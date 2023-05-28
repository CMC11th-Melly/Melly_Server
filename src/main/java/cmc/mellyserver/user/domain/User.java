package cmc.mellyserver.user.domain;
import cmc.mellyserver.common.enums.*;
import cmc.mellyserver.common.util.jpa.JpaBaseEntity;
import lombok.*;
import javax.persistence.*;


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

    @Enumerated(EnumType.STRING)
    private Gender gender;

    @Enumerated(EnumType.STRING)
    private AgeGroup ageGroup;

    @Enumerated(EnumType.STRING)
    private Provider provider;

    @Enumerated(EnumType.STRING)
    private RoleType roleType;
    @Embedded
    private Recommend recommend;

    @Column(name = "store_capacity")
    private Double storeCapacity;

    @Enumerated(EnumType.STRING)
    private DeleteStatus isDeleted;

    private String fcmToken;

    private boolean enableAppPush;

    private boolean enableCommentLike;

    private boolean enableComment;

    public void updateUser(String nickname, Gender gender, String profileImage, AgeGroup ageGroup,boolean enableAppPush,boolean enableCommentLike, boolean enableComment)
    {
        this.nickname = nickname;
        this.gender = gender;
        this.profileImage = profileImage;
        this.ageGroup = ageGroup;
        this.enableAppPush = enableAppPush;
        this.enableComment = enableComment;
        this.enableCommentLike = enableCommentLike;
    }

    public void setFcmToken(String fcmToken)
    {
        this.fcmToken = fcmToken;
    }

    public void addSurveyData(RecommendGroup recommendGroup, RecommendPlace recommendPlace, RecommendActivity recommendActivity)
    {
        this.recommend = new Recommend(recommendGroup,recommendPlace,recommendActivity);
    }

    public void setProfileImage(String profileImage) {
        this.profileImage = profileImage;
    }

    public User(String uid, Provider provider, RoleType roleType, String password)
    {
        this.userId  = uid;
        this.provider = provider;
        this.roleType = roleType;
        this.password = password;
    }

    @Builder
    public User(String email,String password,RoleType roleType,String profileImage,AgeGroup ageGroup,Gender gender,String fcmToken,String uid,Provider provider,String nickname,boolean enableAppPush,boolean enableCommentLike, boolean enableComment, DeleteStatus isDeleted)
    {
        this.email = email;
        this.password = password;
        this.roleType = roleType;
        this.profileImage = profileImage;
        this.ageGroup = ageGroup;
        this.gender =gender;
        this.fcmToken = fcmToken;
        this.userId = uid;
        this.provider = provider;
        this.nickname = nickname;
        this.enableAppPush = enableAppPush;
        this.enableComment = enableComment;
        this.enableCommentLike = enableCommentLike;
        this.isDeleted = isDeleted;
    }

    public void updateProfile(String nickname, Gender gender, AgeGroup ageGroup) {
       if(nickname != null)
       {
           this.nickname = nickname;
       }

       if(gender != null)
       {
           this.gender = gender;
       }

       if(ageGroup != null)
       {
           this.ageGroup = ageGroup;
       }
    }

    public void chnageProfileImage(String image)
    {
        this.profileImage = image;
    }

    @PrePersist
    private void init()
    {
        this.isDeleted = DeleteStatus.N;
    }

    public void removeUser()
    {
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
}
