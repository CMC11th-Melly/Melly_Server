package cmc.mellyserver.group.domain;

import cmc.mellyserver.common.util.jpa.JpaBaseEntity;
import cmc.mellyserver.common.enums.GroupType;
import lombok.*;

import javax.persistence.*;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@AllArgsConstructor
public class UserGroup extends JpaBaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "groups_id")
    private Long id; // 그룹 id

    private String groupName; // 그룹 이름

    private int groupIcon; // 그룹 아이콘 구분하는 번호

    private String inviteLink; // 초대 링크

    private Long creatorId; // 초기에 그룹을 만든 사람

    @Enumerated(EnumType.STRING)
    private GroupType groupType; // 그룹 종류

    private boolean isDeleted; // 삭제 여부


    @Builder
    public UserGroup(String groupName, String inviteLink, GroupType groupType, int groupIcon, Long userSeq)
    {
        this.groupName = groupName;
        this.inviteLink = inviteLink;
        this.groupType =groupType;
        this.groupIcon = groupIcon;
        this.creatorId = userSeq;
    }


    public void updateUserGroup(String groupName, GroupType groupType, Integer groupIcon) {
        this.groupName = groupName;
        this.groupType = groupType;
        this.groupIcon = groupIcon;
    }


    @PrePersist
    private void init()
    {
        this.isDeleted = false;
    }


    public void remove()
    {
        this.isDeleted = true;
    }


    public void setCreatorId(Long userId)
    {
        this.creatorId = userId;
    }


}
