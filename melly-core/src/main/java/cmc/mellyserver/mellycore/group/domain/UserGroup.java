package cmc.mellyserver.mellycore.group.domain;

import cmc.mellyserver.mellycommon.enums.GroupType;
import cmc.mellyserver.mellycore.common.util.jpa.JpaBaseEntity;
import lombok.*;

import javax.persistence.*;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Table(name = "tb_user_group")
@AllArgsConstructor
public class UserGroup extends JpaBaseEntity {

    @Id
    @Column(name = "group_id", nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "group_name", nullable = false)
    private String groupName;

    @Column(name = "group_icon", nullable = false)
    private Integer groupIcon;

    @Column(name = "invite_link")
    private String inviteLink;

    @Version
    private Long version;


    @Enumerated(EnumType.STRING)
    @Column(name = "group_type", nullable = false)
    private GroupType groupType;

    @Column(name = "is_deleted", nullable = false)
    private Boolean isDeleted;

    @Builder
    public UserGroup(String groupName, String inviteLink, GroupType groupType, int groupIcon) {
        this.groupName = groupName;
        this.inviteLink = inviteLink;
        this.groupType = groupType;
        this.groupIcon = groupIcon;
    }


    public void remove() {

        this.isDeleted = Boolean.TRUE;
    }

    public void update(String groupName, GroupType groupType, Integer groupIcon) {

        this.groupName = groupName;
        this.groupType = groupType;
        this.groupIcon = groupIcon;
    }


    @PrePersist
    private void init() {
        this.isDeleted = Boolean.FALSE;
    }
}
