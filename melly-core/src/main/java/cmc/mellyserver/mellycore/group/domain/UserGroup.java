package cmc.mellyserver.mellycore.group.domain;

import cmc.mellyserver.mellycore.common.enums.DeleteStatus;
import cmc.mellyserver.mellycore.common.enums.GroupType;
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
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String groupName;

    private Integer groupIcon;

    private String inviteLink;

    private Long creatorId;

    @Enumerated(EnumType.STRING)
    private GroupType groupType;

    private DeleteStatus isDeleted;

    @Builder
    public UserGroup(String groupName, String inviteLink, GroupType groupType, int groupIcon,
                     Long userSeq) {
        this.groupName = groupName;
        this.inviteLink = inviteLink;
        this.groupType = groupType;
        this.groupIcon = groupIcon;
        this.creatorId = userSeq;
    }

    public void remove(Long userId) {

        if (!isGroupOwner(userId)) {
            throw new IllegalArgumentException("그룹 생성자가 아니기에 삭제 권한이 없습니다.");
        }
        this.isDeleted = DeleteStatus.Y;
    }

    public void updateUserGroup(Long userId, String groupName, GroupType groupType, Integer groupIcon) {

        if (!isGroupOwner(userId)) {
            throw new IllegalArgumentException("그룹 생성자가 아니기에 수정 권한이 없습니다.");
        }
        this.groupName = groupName;
        this.groupType = groupType;
        this.groupIcon = groupIcon;
    }

    public void assignGroupOwner(Long userId) {
        this.creatorId = userId;
    }

    private boolean isGroupOwner(Long userId) {
        return this.creatorId.equals(userId);
    }

    @PrePersist
    private void init() {
        this.isDeleted = DeleteStatus.N;
    }
}
