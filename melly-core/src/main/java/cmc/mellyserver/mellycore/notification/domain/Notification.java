package cmc.mellyserver.mellycore.notification.domain;

import cmc.mellyserver.mellycommon.enums.NotificationType;
import cmc.mellyserver.mellycore.common.util.jpa.JpaBaseEntity;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;


@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Table(name = "tb_notification")
public class Notification extends JpaBaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "notification_id")
    private Long id;

    @Column(name = "title")
    private String title;

    @Column(name = "message")
    private String message;

    @Enumerated(EnumType.STRING)
    NotificationType notificationType;

    @Column(name = "user_checked")
    private Boolean userChecked;

    @Column(name = "user_id")
    private Long userId;

    @Column(name = "memory_id")
    private Long memoryId;

    public Notification(String title, String message, NotificationType notificationType, boolean checked, Long userId, Long memoryId) {
        this.title = title;
        this.message = message;
        this.notificationType = notificationType;
        this.userChecked = checked;
        this.userId = userId;
        this.memoryId = memoryId;
    }

    public static Notification createNotification(String title, String message, NotificationType notificationType, boolean checked, Long memoryId, Long userId) {

        return new Notification(title, message, notificationType, checked, userId, memoryId);
    }

    public void userCheckedNotification() {
        this.userChecked = true;
    }
}
