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
    @Column(name = "groups_id")
    private Long id;

    private String groupName;

    private String inviteLink;

    // 하나의 메모리는 무조건 하나의 그룹에 속함. 따라서 타입은 grouptype으로 판단하면 될듯!
    @Enumerated(EnumType.STRING)
    private GroupType groupType;

    @OneToMany(mappedBy = "group",fetch = FetchType.LAZY)
    private List<GroupAndUser> groupAndUsers = new ArrayList<>();





}
