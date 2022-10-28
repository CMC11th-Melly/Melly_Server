package cmc.mellyserver.group.domain;

import cmc.mellyserver.common.util.jpa.JpaBaseEntity;
import cmc.mellyserver.group.domain.enums.GroupType;
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

    private int groupIcon;

    private String inviteLink;

    // 하나의 메모리는 무조건 하나의 그룹에 속함. 따라서 타입은 grouptype으로 판단하면 될듯!
    @Enumerated(EnumType.STRING)
    private GroupType groupType;

    // 그룹 삭제되면 자동으로 삭제
    @OneToMany(mappedBy = "group",fetch = FetchType.LAZY,cascade = CascadeType.ALL,orphanRemoval = true)
    private List<GroupAndUser> groupAndUsers = new ArrayList<>();

    @Builder
    public UserGroup(String groupName, String inviteLink,GroupType groupType,int groupIcon)
    {
        this.groupName = groupName;
        this.inviteLink = inviteLink;
        this.groupType =groupType;
        this.groupIcon = groupIcon;
    }


    public void setGroupUser(GroupAndUser groupUser)
    {
        groupAndUsers.add(groupUser);
        groupUser.setGroup(this);
    }

    public void updateUserGroup(String groupName, GroupType groupType, Integer groupIcon)
    {
        if(groupName != null)
        {
            this.groupName = groupName;
        }

        if(groupType != null)
        {
            this.groupType = groupType;
        }

        if(groupIcon != null)
        {
            this.groupIcon = groupIcon;
        }


    }





}
