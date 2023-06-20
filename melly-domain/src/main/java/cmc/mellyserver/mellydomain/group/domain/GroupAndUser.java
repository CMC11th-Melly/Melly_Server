package cmc.mellyserver.mellydomain.group.domain;

import cmc.mellyserver.mellydomain.user.domain.User;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

/**
 * GroupAndUser.java
 *
 * @author jemlog
 */
@Entity
@NoArgsConstructor
@Table(name = "tb_group_and_user")
@Getter
public class GroupAndUser {

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

