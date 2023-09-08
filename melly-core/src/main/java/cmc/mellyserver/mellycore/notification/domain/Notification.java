package cmc.mellyserver.mellycore.notification.domain;

import cmc.mellyserver.mellycore.notification.domain.enums.NotificationType;
import lombok.Builder;
import lombok.Getter;

import javax.persistence.*;
import java.time.LocalDateTime;


@Getter
@Entity
@Table(name = "tb_notification")
public class Notification {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "user_id")
    private Long userId; // 이걸 기반으로 조회

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
    public Notification(final String content, final Long userId, final NotificationType notificationType, final Boolean isRead, final String profileImage, final String nickname, final Long memoryId, final LocalDateTime createdDateTime) {
        this.content = content;
        this.userId = userId;
        this.notificationType = notificationType;
        this.isRead = isRead;
        this.profileImage = profileImage;
        this.nickname = nickname;
        this.memoryId = memoryId;
        this.createdDateTime = createdDateTime;
    }

    public static Notification createNotification(final String content, final Long userId, final NotificationType notificationType, final Boolean isRead, final String profileImage, final String nickname, final Long memoryId, final LocalDateTime createdDateTime) {

        return new Notification(content, userId, notificationType, isRead, profileImage, nickname, memoryId, createdDateTime);
    }

    public void userCheckedNotification() {
        this.isRead = Boolean.TRUE;
    }
}
