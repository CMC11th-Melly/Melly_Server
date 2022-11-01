package cmc.mellyserver.notification.domain;

import cmc.mellyserver.common.util.jpa.JpaBaseEntity;
import cmc.mellyserver.user.domain.User;
import lombok.AccessLevel;
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





}
