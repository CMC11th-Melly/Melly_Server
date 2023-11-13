package cmc.mellyserver.dbcore.group;

import java.time.LocalDateTime;

import cmc.mellyserver.dbcore.config.jpa.JpaBaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.Version;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "tb_user_group")
@Entity
public class UserGroup extends JpaBaseEntity {

	@Id
	@Column(name = "group_id")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(name = "name")
	private String name;

	@Column(name = "group_icon")
	private int icon;

	@Column(name = "invite_link")
	private String inviteLink;

	@Enumerated(EnumType.STRING)
	@Column(name = "group_type")
	private GroupType groupType;

	@Column(name = "deleted_at")
	private LocalDateTime deletedAt;

	@Version
	private Long version;

	@Builder
	public UserGroup(String name, String inviteLink, GroupType groupType, int icon) {
		this.name = name;
		this.inviteLink = inviteLink;
		this.groupType = groupType;
		this.icon = icon;
	}

	public void delete() {
		this.deletedAt = LocalDateTime.now();
	}

	public void update(String name, GroupType groupType, int icon) {
		this.name = name;
		this.groupType = groupType;
		this.icon = icon;
	}
}
