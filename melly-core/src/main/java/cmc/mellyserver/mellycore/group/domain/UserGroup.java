package cmc.mellyserver.mellycore.group.domain;

import cmc.mellyserver.mellycommon.enums.DeleteStatus;
import cmc.mellyserver.mellycommon.enums.GroupType;
import cmc.mellyserver.mellycore.common.util.jpa.JpaBaseEntity;
import lombok.*;
import org.springframework.util.Assert;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

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

    @ElementCollection
    @CollectionTable
    private Set<Long> groupManagers = new HashSet<>();

    @Enumerated(EnumType.STRING)
    @Column(name = "group_type", nullable = false)
    private GroupType groupType;

    @Column(name = "is_deleted", nullable = false)
    private DeleteStatus isDeleted;

    @Builder
    public UserGroup(String groupName, String inviteLink, GroupType groupType, int groupIcon) {
        this.groupName = groupName;
        this.inviteLink = inviteLink;
        this.groupType = groupType;
        this.groupIcon = groupIcon;
    }

    public void remove(Long userId) {

        if (!isGroupManager(userId)) {
            throw new IllegalArgumentException("그룹 관리자가 아니기에 삭제 권한이 없습니다.");
        }
        this.isDeleted = DeleteStatus.Y;
    }

    public void update(Long userId, String groupName, GroupType groupType, Integer groupIcon) {

        if (!isGroupManager(userId)) {
            throw new IllegalArgumentException("그룹 관리자가 아니기에 수정 권한이 없습니다.");
        }
        this.groupName = groupName;
        this.groupType = groupType;
        this.groupIcon = groupIcon;
    }
    
    public void assignGroupManager(Long userId) {
        this.groupManagers.add(userId);
    }

    private boolean isGroupManager(Long userId) {
        return this.groupManagers.contains(userId);
    }

    /**
     * 낙관적 락, 분산락 적용 지점
     */
    private void removeGroupManager(Long managerId, Long userId) {

        if (!isGroupManager(managerId)) {
            throw new IllegalArgumentException("그룹 관리자가 아니기에 수정 권한이 없습니다.");
        }

        Assert.isTrue(groupManagers.size() > 1, () -> {
            throw new IllegalArgumentException("그룹 매니저는 최소 1명 이상 존재해야 합니다.");
        });

        groupManagers.remove(userId);
    }

    @PrePersist
    private void init() {
        this.isDeleted = DeleteStatus.N;
    }
}
