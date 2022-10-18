package cmc.mellyserver.user.domain;
import cmc.mellyserver.auth.presentation.dto.Provider;
import cmc.mellyserver.common.util.jpa.JpaBaseEntity;
import cmc.mellyserver.group.domain.GroupAndUser;
import cmc.mellyserver.memory.domain.Memory;
import cmc.mellyserver.scrap.domain.Scrap;
import cmc.mellyserver.user.domain.enums.AgeGroup;
import cmc.mellyserver.user.domain.enums.Gender;
import cmc.mellyserver.user.domain.enums.RoleType;
import lombok.*;
import org.springframework.web.multipart.MultipartFile;

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
    private List<Scrap> scraps = new ArrayList<>();

    public void updateUser(String nickname, Gender gender, String profileImage, AgeGroup ageGroup)
    {
        this.nickname = nickname;
        this.gender = gender;
        this.profileImage = profileImage;
        this.ageGroup = ageGroup;
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

    public void updateProfile(String nickname, String image) {
       if(nickname != null)
       {
           this.nickname = nickname;
       }

       if(image != null)
       {
           this.profileImage = image;
       }
    }
}
