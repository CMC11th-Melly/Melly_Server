package cmc.mellyserver.user.domain;
import cmc.mellyserver.auth.presentation.dto.Provider;
import cmc.mellyserver.common.enums.AgeGroup;
import cmc.mellyserver.common.enums.Gender;
import cmc.mellyserver.common.enums.RecommendGroup;
import cmc.mellyserver.common.enums.RoleType;
import cmc.mellyserver.common.util.jpa.JpaBaseEntity;
import lombok.*;
import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;


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

    @Column(name = "user_id",nullable = false)
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
    private Double storeCapacity;  // 아직 필요한게 맞는지는 모르겠다.

    private String fcmToken; // 푸시 위한 토큰

    // 푸시 알림 허용 목록
    private boolean enableAppPush;

    private boolean enableCommentLike;

    private boolean enableComment;

    @ElementCollection
    @CollectionTable
    private List<Long> participatedGroupId = new ArrayList<>();


//
//    // TODO : 관계 끊기
//    @OneToMany(mappedBy = "user",fetch = FetchType.LAZY,orphanRemoval = true,cascade = CascadeType.REMOVE)
//    private List<MemoryScrap> memoryScraps = new ArrayList<>();
//
//    // TODO : 관계 끊기
//    @OneToMany(mappedBy = "user",fetch = FetchType.LAZY,orphanRemoval = true,cascade = CascadeType.REMOVE)
//    private List<CommentLike> commentLikes = new ArrayList<>();
//
//    // TODO : 관계 끊기
//    @OneToMany(mappedBy = "writer",fetch = FetchType.LAZY,orphanRemoval = true)
//    private List<Comment> comments = new ArrayList<>();
//
//    // TODO : 알림
//    @OneToMany(mappedBy = "user",fetch = FetchType.LAZY,cascade = CascadeType.REMOVE,orphanRemoval = true)
//    private List<Notification> notifications = new ArrayList<>();
//
//    // TODO : 끊기
//    @OneToMany(mappedBy = "user",fetch = FetchType.LAZY)
//    private List<MemoryReport> memoryReports = new ArrayList<>();
//
//    // TODO : 끊기
//    @OneToMany(mappedBy = "user",fetch = FetchType.LAZY)
//    private List<CommentReport> commentReports = new ArrayList<>();
//
//    // TODO : 끊기
//    @OneToMany(mappedBy = "user",fetch = FetchType.LAZY)
//    private List<MemoryBlock> memoryBlocks = new ArrayList<>();
//
//    @CollectionTable
//    private List<Long> blockedMemoryIds = new ArrayList<>();
//
//    // TODO : 끊기
//    @OneToMany(mappedBy = "user",fetch = FetchType.LAZY)
//    private List<CommentBlock> commentBlocks = new ArrayList<>();
//
//
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

    public void addPollData(RecommendGroup recommendGroup, String recommendPlace, String recommendActivity)
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

       if(image != null)
       {
           this.profileImage = image;
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
