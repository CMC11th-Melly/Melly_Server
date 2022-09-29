package cmc.mellyserver.user.domain;
import cmc.mellyserver.auth.presentation.dto.Provider;
import cmc.mellyserver.common.util.JpaBaseEntity;
import cmc.mellyserver.group.domain.GroupAndUser;
import cmc.mellyserver.memory.domain.Memory;
import cmc.mellyserver.place.domain.Scrap;
import lombok.*;
import javax.persistence.*;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Builder
@AllArgsConstructor
public class User extends JpaBaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_seq")
    private Long userSeq;

    // SocialId or 일반 로그인 시 UUID 추가
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

    @OneToMany(mappedBy = "user",fetch = FetchType.LAZY)
    private List<Memory> memories = new ArrayList<>();

    @OneToMany(mappedBy = "user",fetch = FetchType.LAZY)
    private List<GroupAndUser> groupAndUsers = new ArrayList<>();

    @OneToMany(mappedBy = "user",fetch = FetchType.LAZY)
    private List<Scrap> scraps = new ArrayList<>();

    public void updateUser(String nickname, Gender gender, String profileImage, AgeGroup ageGroup)
    {
        this.nickname = nickname;
        this.gender = gender;
        this.profileImage = profileImage;
        this.ageGroup = ageGroup;
    }
}
