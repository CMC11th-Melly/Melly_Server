package cmc.mellyserver.group.domain.repository;

import cmc.mellyserver.group.domain.GroupAndUser;
import cmc.mellyserver.group.domain.UserGroup;
import cmc.mellyserver.user.domain.User;
import io.lettuce.core.dynamic.annotation.Param;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

/**
 * GroupAndUserRepository.java
 *
 * @author jemlog
 */
public interface GroupAndUserRepository extends JpaRepository<GroupAndUser,Long> {

    Optional<GroupAndUser> findGroupAndUserByUserAndGroup(User user, UserGroup userGroup);

    @Query("select distinct ug from GroupAndUser ga join UserGroup ug on ug.id = ga.group.id join User u on u.userSeq = ga.user.userSeq where u.userSeq = :userId")
    List<UserGroup> findUserGroupLoginUserAssociated(@Param("userId") Long userId);
}
