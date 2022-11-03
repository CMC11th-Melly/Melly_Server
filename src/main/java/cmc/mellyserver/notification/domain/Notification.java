package cmc.mellyserver.notification.domain;

import cmc.mellyserver.common.util.jpa.JpaBaseEntity;
import cmc.mellyserver.memory.domain.Memory;
import cmc.mellyserver.user.domain.User;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Notification extends JpaBaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    NotificationType title;

    private String message;

    private boolean checked;

    @ManyToOne
    @JoinColumn(name = "user_seq")
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "memory_id")
    private Memory memory;

    public Notification(NotificationType title, String message, boolean checked) {
        this.title = title;
        this.message = message;
        this.checked = checked;
    }

    public void checkNotification(boolean check)
    {
        this.checked = check;
    }

    private void setUser(User user)
    {
        this.user = user;
        user.getNotifications().add(this);
    }

    private void setMemory(Memory memory)
    {
        this.memory = memory;
        memory.getNotifications().add(this);
    }

    public static Notification createNotification(NotificationType title, String message, boolean checked,Memory memory,User user)
    {
        Notification notification = new Notification(title, message, checked);
        notification.setMemory(memory);
        notification.setUser(user);
        return notification;
    }
}
