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
    private Long userSeq; // 유저 DB 식별자

    @Column(name = "user_id")
    private String userId; // 유저 OAuth 아이디

    private String email; // 유저 이메일

    private String password; // 유저 비밀번호

    private String nickname; // 유저 닉네임

    private String profileImage; // 유저 프로필 이미지

    @Enumerated(EnumType.STRING)
    private Gender gender; // 유저 성별

    @Enumerated(EnumType.STRING)
    private AgeGroup ageGroup; // 유저 연령

    @Enumerated(EnumType.STRING)
    private Provider provider; // 유저 제공자

    @Enumerated(EnumType.STRING)
    private RoleType roleType; // 유저 역할
    @Embedded
    private Recommend recommend; // 유저 추천 정보

    @Column(name = "store_capacity")
    private Double storeCapacity;  // 저장한 이미지 용량

    private String fcmToken; // 푸시 위한 토큰

    private boolean isdeleted;

    // 푸시 알림 목록
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

    // 초기 회원가입시, 회원 맞춤 정보를 제공하기 위함
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
    public User(String email,String password,RoleType roleType,String profileImage,AgeGroup ageGroup,Gender gender,String fcmToken,String uid,Provider provider,String nickname,boolean enableAppPush,boolean enableCommentLike, boolean enableComment)
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
    }

    public void updateProfile(String nickname, Gender gender, AgeGroup ageGroup, String image) {
       if(nickname != null)
       {
           this.nickname = nickname;
       }


       this.profileImage = image;


       if(gender != null)
       {
           this.gender = gender;
       }

       if(ageGroup != null)
       {
           this.ageGroup = ageGroup;
       }
    }

    @PrePersist
    private void init()
    {
        this.isdeleted = false;
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
