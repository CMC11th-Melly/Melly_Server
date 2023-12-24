package cmc.mellyserver.dbcore.group;

import cmc.mellyserver.dbcore.config.jpa.JpaBaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@Table(name = "tb_group_and_user")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class GroupAndUser extends JpaBaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "group_and_user_id")
    private Long id;

    @Column(name = "user_id")
    private Long userId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "groups_id")
    private UserGroup group;

    @Builder
    public GroupAndUser(Long userId, UserGroup group) {
        this.userId = userId;
        this.group = group;
    }

    public static GroupAndUser of(Long userId, UserGroup group) {
        return GroupAndUser.builder().userId(userId).group(group).build();
    }
}
