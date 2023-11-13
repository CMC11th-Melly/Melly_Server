package cmc.mellyserver.dbcore.notification;

import java.time.LocalDateTime;

import cmc.mellyserver.dbcore.config.jpa.JpaBaseEntity;
import cmc.mellyserver.dbcore.notification.enums.NotificationType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "tb_notification")
public class Notification extends JpaBaseEntity {

  @Column(name = "notification_type")
  NotificationType notificationType;
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;
  @Column(name = "user_id")
  private Long userId;
  @Column(name = "content")
  private String content;
  @Column(name = "is_read")
  private boolean isRead;

  @Column(name = "profile_image")
  private String profileImage;

  @Column(name = "nickname")
  private String nickname;

  @Column(name = "memory_id")
  private Long memoryId;

  @Column(name = "deleted_at")
  private LocalDateTime deletedAt;

  @Builder
  public Notification(String content, Long userId, NotificationType notificationType, boolean isRead,
	  String profileImage, String nickname, Long memoryId) {
	this.content = content;
	this.userId = userId;
	this.notificationType = notificationType;
	this.isRead = isRead;
	this.profileImage = profileImage;
	this.nickname = nickname;
	this.memoryId = memoryId;
  }

  public static Notification createNotification(String content, Long userId, NotificationType notificationType,
	  Boolean isRead, String profileImage,
	  String nickname, Long memoryId) {

	return Notification.builder()
		.content(content)
		.userId(userId)
		.notificationType(notificationType)
		.isRead(isRead)
		.profileImage(profileImage)
		.nickname(nickname)
		.memoryId(memoryId)
		.build();
  }

  public boolean isRead() {
	return isRead;
  }

  public void read() {
	this.isRead = true;
  }

  public void delete() {
	this.deletedAt = LocalDateTime.now();
  }

}
