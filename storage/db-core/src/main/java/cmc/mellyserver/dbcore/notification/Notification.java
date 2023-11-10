package cmc.mellyserver.dbcore.notification;

import cmc.mellyserver.dbcore.notification.enums.NotificationType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.time.LocalDateTime;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "tb_notification")
public class Notification {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(name = "user_id")
	private Long userId;

	@Column(name = "content")
	private String content;

	@Column(name = "notification_type")
	NotificationType notificationType;

	@Column(name = "is_read")
	private Boolean isRead;

	@Column(name = "profile_image")
	private String profileImage;

	@Column(name = "nickname")
	private String nickname;

	@Column(name = "memory_id")
	private Long memoryId;

	@Column(name = "notification_date_time")
	private LocalDateTime createdDateTime;

	@Builder
	public Notification(final String content, final Long userId, final NotificationType notificationType,
			final Boolean isRead, final String profileImage, final String nickname, final Long memoryId,
			final LocalDateTime createdDateTime) {
		this.content = content;
		this.userId = userId;
		this.notificationType = notificationType;
		this.isRead = isRead;
		this.profileImage = profileImage;
		this.nickname = nickname;
		this.memoryId = memoryId;
		this.createdDateTime = createdDateTime;
	}

	public static Notification createNotification(final String content, final Long userId,
			final NotificationType notificationType, final Boolean isRead, final String profileImage,
			final String nickname, final Long memoryId, final LocalDateTime createdDateTime) {

		return new Notification(content, userId, notificationType, isRead, profileImage, nickname, memoryId,
				createdDateTime);
	}

	public boolean isRead(){
		return isRead;
	}

	public void read() {
		this.isRead = Boolean.TRUE;
	}

}
