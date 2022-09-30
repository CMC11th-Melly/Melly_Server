package cmc.mellyserver.group.domain;

import cmc.mellyserver.common.util.JpaBaseEntity;
import cmc.mellyserver.user.domain.User;
import lombok.*;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@AllArgsConstructor
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

    @OneToMany(mappedBy = "group",fetch = FetchType.LAZY,cascade = CascadeType.PERSIST)
    private List<GroupAndUser> groupAndUsers = new ArrayList<>();

    @Builder
    public UserGroup(String groupName, String inviteLink,GroupType groupType)
    {
        this.groupName = groupName;
        this.inviteLink = inviteLink;
        this.groupType =groupType;
    }


    public void setGroupUser(GroupAndUser groupUser)
    {
        groupAndUsers.add(groupUser);
        groupUser.setGroup(this);
    }




}
