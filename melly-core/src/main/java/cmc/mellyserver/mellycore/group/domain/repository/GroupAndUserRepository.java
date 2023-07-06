package cmc.mellyserver.mellycore.group.domain.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import cmc.mellyserver.mellycore.group.domain.GroupAndUser;
import cmc.mellyserver.mellycore.group.domain.UserGroup;
import cmc.mellyserver.mellycore.user.domain.User;

/**
 * GroupAndUserRepository.java
 *
 * @author jemlog
 */
public interface GroupAndUserRepository extends JpaRepository<GroupAndUser, Long> {

	Optional<GroupAndUser> findGroupAndUserByUserAndGroup(User user, UserGroup userGroup);

	@Query("select distinct ug from GroupAndUser ga join UserGroup ug on ug.id = ga.group.id join User u on u.userSeq = ga.user.userSeq where u.userSeq = :userId")
	List<UserGroup> findUserGroupLoginUserAssociated(@Param("userId") Long userId);
}
