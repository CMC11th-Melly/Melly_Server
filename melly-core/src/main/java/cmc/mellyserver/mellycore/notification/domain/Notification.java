package cmc.mellyserver.mellycore.notification.domain;

import cmc.mellyserver.mellycore.notification.domain.enums.NotificationType;
import lombok.Builder;
import lombok.Getter;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import javax.persistence.Id;
import java.time.LocalDateTime;


@Getter
@Document(collection = "notification")
public class Notification {

    @Id
    private String id;

    @Field(name = "user_id")
    private Long userId; // 이걸 기반으로 조회

    @Field(name = "content")
    private String content;

    @Field(name = "notification_type")
    NotificationType notificationType;

    @Field(name = "is_read")
    private Boolean isRead;

    @Field(name = "profile_image")
    private String profileImage;

    @Field(name = "nickname")
    private String nickname;

    @Field(name = "memory_id")
    private Long memoryId;

    @Field(name = "notification_date_time")
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
