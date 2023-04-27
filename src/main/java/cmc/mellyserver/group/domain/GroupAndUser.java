package cmc.mellyserver.group.domain;

import cmc.mellyserver.user.domain.User;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.Objects;

/**
 * GroupAndUser.java
 *
 * @author jemlog
 */
@Entity
@NoArgsConstructor
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

    public void setGroup(UserGroup group)
    {
        this.group = group;
    }

    public void setUser(User user)
    {
        this.user = user;
    }


    public GroupAndUser(User user, UserGroup group) {
        this.user = user;
        this.group = group;
    }
<<<<<<< HEAD

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        GroupAndUser that = (GroupAndUser) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
=======
}
>>>>>>> fix
