package cmc.mellyserver.user.domain;
import cmc.mellyserver.auth.presentation.dto.Provider;
import cmc.mellyserver.common.util.JpaBaseEntity;
import cmc.mellyserver.group.domain.GroupAndUser;
import lombok.*;
import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

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

    private boolean gender;
    // 추천 기능 만들 때 필요

    private AgeGroup ageGroup;

    @Enumerated(EnumType.STRING)
    private Provider provider;

    @Enumerated(EnumType.STRING)
    private RoleType roleType;

    @OneToMany(mappedBy = "user",fetch = FetchType.LAZY)
    private List<GroupAndUser> groupAndUsers = new ArrayList<>();
}
