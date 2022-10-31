package cmc.mellyserver.user.domain;
import cmc.mellyserver.auth.presentation.dto.Provider;
import cmc.mellyserver.comment.domain.Comment;
import cmc.mellyserver.comment.domain.CommentLike;
import cmc.mellyserver.common.util.jpa.JpaBaseEntity;
import cmc.mellyserver.group.domain.GroupAndUser;
import cmc.mellyserver.memory.domain.Memory;
import cmc.mellyserver.memoryScrap.domain.MemoryScrap;
import cmc.mellyserver.notification.domain.Notification;
import cmc.mellyserver.placeScrap.domain.PlaceScrap;
import cmc.mellyserver.user.domain.enums.*;
import lombok.*;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@AllArgsConstructor
public class User extends JpaBaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_seq")
    private Long userSeq;

    @Column(name = "user_id",nullable = false)
    private String userId;

    private String email;

    private String password;

    private String nickname;

    private String profileImage;

    @ElementCollection
    @CollectionTable(
            name = "visited_place_table",
            joinColumns = @JoinColumn(name = "user_id"))
    @Column(name = "visited_place") // 컬럼명 지정 (예외)
    private Set<Long> visitedPlace = new HashSet<>();

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

    private String fcmToken;

    @OneToMany(mappedBy = "user",fetch = FetchType.LAZY,orphanRemoval = true,cascade = CascadeType.REMOVE)
    private List<Memory> memories = new ArrayList<>();

    @OneToMany(mappedBy = "user",fetch = FetchType.LAZY,cascade = CascadeType.REMOVE)
    private List<GroupAndUser> groupAndUsers = new ArrayList<>();

    @OneToMany(mappedBy = "user",fetch = FetchType.LAZY,orphanRemoval = true,cascade = CascadeType.REMOVE)
    private List<PlaceScrap> placeScraps = new ArrayList<>();

    @OneToMany(mappedBy = "user",fetch = FetchType.LAZY,orphanRemoval = true,cascade = CascadeType.REMOVE)
    private List<MemoryScrap> memoryScraps = new ArrayList<>();

    @OneToMany(mappedBy = "user",fetch = FetchType.LAZY,orphanRemoval = true,cascade = CascadeType.REMOVE)
    private List<CommentLike> commentLikes = new ArrayList<>();

    @OneToMany(mappedBy = "writer",fetch = FetchType.LAZY,orphanRemoval = true)
    private List<Comment> comments = new ArrayList<>();

    @OneToMany(mappedBy = "user",fetch = FetchType.LAZY,cascade = CascadeType.REMOVE,orphanRemoval = true)
    private List<Notification> notifications = new ArrayList<>();

    public void updateUser(String nickname, Gender gender, String profileImage, AgeGroup ageGroup)
    {
        this.nickname = nickname;
        this.gender = gender;
        this.profileImage = profileImage;
        this.ageGroup = ageGroup;
    }

    public void addPollData(RecommendGroup recommendGroup, RecommendPlace recommendPlace, RecommendActivity recommendActivity)
    {
        this.recommend = new Recommend(recommendGroup,recommendPlace,recommendActivity);
    }


    public User(String uid, Provider provider,RoleType roleType,String password)
    {
        this.userId  = uid;
        this.provider = provider;
        this.roleType = roleType;
        this.password = password;
    }

    @Builder
    public User(String email,String password,RoleType roleType,String profileImage,AgeGroup ageGroup,Gender gender,String uid,Provider provider,String nickname)
    {
        this.email = email;
        this.password = password;
        this.roleType = roleType;
        this.profileImage = profileImage;
        this.ageGroup = ageGroup;
        this.gender =gender;
        this.userId = uid;
        this.provider = provider;
        this.nickname = nickname;
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
}
