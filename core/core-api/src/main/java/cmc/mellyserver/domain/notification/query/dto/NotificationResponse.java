package cmc.mellyserver.domain.notification.query.dto;

import cmc.mellyserver.dbcore.notification.Notification;
import cmc.mellyserver.dbcore.notification.enums.NotificationType;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
public class NotificationResponse {

    private Long memoryId;

    private String content;

    NotificationType notificationType;

    private Boolean isRead;

    private String profileImage;

    private String nickname;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private LocalDateTime createdDateTime;

    public static NotificationResponse from(Notification notification) {
        return new NotificationResponse(notification.getMemoryId(), notification.getContent(), notification.getNotificationType(), notification.getIsRead(), notification.getProfileImage(), notification.getNickname(), notification.getCreatedDateTime());
    }
}
