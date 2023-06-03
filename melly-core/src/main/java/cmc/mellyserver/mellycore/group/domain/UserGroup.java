package cmc.mellyserver.mellycore.group.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.PrePersist;
import javax.persistence.Table;

import cmc.mellyserver.mellycore.common.enums.GroupType;
import cmc.mellyserver.mellycore.common.util.jpa.JpaBaseEntity;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Table(name = "tb_user_group")
@AllArgsConstructor
public class UserGroup extends JpaBaseEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "group_id")
	private Long id; // 그룹 id

	private String groupName; // 그룹 이름

	private int groupIcon; // 그룹 아이콘 구분하는 번호

	private String inviteLink; // 초대 링크

	private Long creatorId; // 초기에 그룹을 만든 사람

	@Enumerated(EnumType.STRING)
	private GroupType groupType; // 그룹 종류

	private boolean isDeleted; // 삭제 여부

	@Builder
	public UserGroup(String groupName, String inviteLink, GroupType groupType, int groupIcon, Long userSeq) {
		this.groupName = groupName;
		this.inviteLink = inviteLink;
		this.groupType = groupType;
		this.groupIcon = groupIcon;
		this.creatorId = userSeq;
	}

	public void updateUserGroup(String groupName, GroupType groupType, Integer groupIcon) {
		this.groupName = groupName;
		this.groupType = groupType;
		this.groupIcon = groupIcon;
	}

	@PrePersist
	private void init() {
		this.isDeleted = false;
	}

	public void remove() {
		this.isDeleted = true;
	}

	public void setCreatorId(Long userId) {
		this.creatorId = userId;
	}

}
