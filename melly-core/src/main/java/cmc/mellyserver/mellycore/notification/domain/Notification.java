package cmc.mellyserver.mellycore.notification.domain;

import cmc.mellyserver.mellycore.notification.domain.enums.NotificationType;
import lombok.Getter;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import javax.persistence.Column;
import javax.persistence.Id;


@Getter
@Document(collection = "notification")
public class Notification {

    @Id
    private Long id;

    @Field(name = "title")
    private String title;

    @Field(name = "content")
    private String content;

    @Field(name = "notification_type")
    NotificationType notificationType;

    @Field(name = "is_read")
    private Boolean isRead;

    @Column(name = "user_id")
    private Long userId;

    @Column(name = "memory_id")
    private Long memoryId;

    public Notification(String title, String content, NotificationType notificationType, boolean isRead, Long userId, Long memoryId) {
        this.title = title;
        this.content = content;
        this.notificationType = notificationType;
        this.isRead = isRead;
        this.userId = userId;
        this.memoryId = memoryId;
    }

    public static Notification createNotification(String title, String message, NotificationType notificationType, boolean checked, Long memoryId, Long userId) {

        return new Notification(title, message, notificationType, checked, userId, memoryId);
    }

    public void userCheckedNotification() {
        this.isRead = Boolean.TRUE;
    }
}
