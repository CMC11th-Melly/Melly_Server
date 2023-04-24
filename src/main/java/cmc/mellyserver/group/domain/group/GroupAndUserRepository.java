package cmc.mellyserver.group.domain.group;

import cmc.mellyserver.group.domain.GroupAndUser;
import cmc.mellyserver.user.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

/**
 * GroupAndUserRepository.java
 *
 * @author jemlog
 */
public interface GroupAndUserRepository extends JpaRepository<GroupAndUser,Long> {

    Optional<GroupAndUser> findGroupAndUserByUserAndGroup(User user,UserGroup userGroup);
}
