package cmc.mellyserver.notification.domain;

import cmc.mellyserver.common.util.jpa.JpaBaseEntity;
import cmc.mellyserver.user.domain.User;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Notification extends JpaBaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;

    private String link;

    private String message;

    private boolean checked;

    @ManyToOne
    @JoinColumn(name = "user_seq")
    private User user;

    @Enumerated(EnumType.STRING)
    NotificationType notificationType;



}
