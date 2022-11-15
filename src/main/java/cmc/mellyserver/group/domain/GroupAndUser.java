package cmc.mellyserver.group.domain;

import cmc.mellyserver.user.domain.User;
import lombok.*;

import javax.persistence.*;

@Entity
@NoArgsConstructor
@Getter
public class GroupAndUser {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "group_and_user_id")
    private Long id;

    @ManyToOne
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
        this.user= user;
        user.getGroupAndUsers().add(this);
    }
}
