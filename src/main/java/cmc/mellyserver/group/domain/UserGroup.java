package cmc.mellyserver.group.domain;

import cmc.mellyserver.common.util.JpaBaseEntity;
import cmc.mellyserver.user.domain.User;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class UserGroup extends JpaBaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_group_id")
    private Long id;

    private String inviteLink;

    // user와 그룹은 별개, 한번 가족이라는 그룹을 설정해놓으면 그 사람들이랑만 공유 가능!
    @Enumerated(EnumType.STRING)
    private GroupType groupType;

    @OneToMany(mappedBy = "group",fetch = FetchType.LAZY)
    private List<User> user = new ArrayList<>();





}
