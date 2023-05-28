package cmc.mellyserver.notification.domain;

import cmc.mellyserver.common.enums.NotificationType;
import cmc.mellyserver.common.util.jpa.JpaBaseEntity;
import cmc.mellyserver.memory.domain.Memory;
import cmc.mellyserver.user.domain.User;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Getter
@Table(name = "tb_notification")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Notification extends JpaBaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "notification_id")
    private Long id;

    @Enumerated(EnumType.STRING)
    NotificationType title;

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

    public void checkNotification(boolean check)
    {
        this.checked = check;
    }


    public static Notification createNotification(NotificationType title, String message, boolean checked,Memory memory,User user)
    {
        Notification notification = new Notification(title, message, checked,user.getUserSeq(),memory.getId());
        return notification;
    }
}
