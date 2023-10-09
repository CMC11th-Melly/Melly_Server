package cmc.mellyserver.dbcore.group;


import cmc.mellyserver.dbcore.config.jpa.JpaBaseEntity;
import cmc.mellyserver.dbcore.user.User;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Table(name = "tb_group_and_user")
public class GroupAndUser extends JpaBaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "group_and_user_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "groups_id")
    private UserGroup group;

    @Builder
    public GroupAndUser(User user, UserGroup group) {
        this.user = user;
        this.group = group;
    }

    public static GroupAndUser of(User user, UserGroup group) {
        return GroupAndUser.builder().user(user).group(group).build();
    }
}

