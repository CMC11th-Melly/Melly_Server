package cmc.mellyserver.mellycore.notification.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import cmc.mellyserver.mellycore.common.enums.NotificationType;
import cmc.mellyserver.mellycore.common.util.jpa.JpaBaseEntity;
import cmc.mellyserver.mellycore.memory.domain.Memory;
import cmc.mellyserver.mellycore.user.domain.User;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@Table(name = "tb_notification")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Notification extends JpaBaseEntity {

	@Enumerated(EnumType.STRING)
	NotificationType title;
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "notification_id")
	private Long id;
	private String message;

	private boolean checked;

	private Long userId;

	private Long memoryId;

	public Notification(NotificationType title, String message, boolean checked, Long userId, Long memoryId) {
		this.title = title;
		this.message = message;
		this.checked = checked;
		this.userId = userId;
		this.memoryId = memoryId;
	}

	public static Notification createNotification(NotificationType title, String message, boolean checked,
		Memory memory, User user) {
		Notification notification = new Notification(title, message, checked, user.getUserSeq(), memory.getId());
		return notification;
	}

	public void checkNotification(boolean check) {
		this.checked = check;
	}
}
