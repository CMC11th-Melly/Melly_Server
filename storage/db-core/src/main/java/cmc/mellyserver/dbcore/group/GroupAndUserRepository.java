package cmc.mellyserver.dbcore.group;


import cmc.mellyserver.dbcore.user.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

/**
 * GroupAndUserRepository.java
 *
 * @author jemlog
 */
public interface GroupAndUserRepository extends JpaRepository<GroupAndUser, Long> {

    Optional<GroupAndUser> findByUserIdAndGroupId(Long userId, Long groupId);

    @Query("select count(*) from GroupAndUser ga where ga.group.id = :groupId")
    Integer countUserParticipatedInGroup(@Param("groupId") Long groupId);

    @Query("select u from GroupAndUser ga inner join User u on u.id = ga.user.id where ga.group.id = :groupId")
    List<User> getUsersParticipatedInGroup(@Param("groupId") Long groupId);

    void deleteByUserIdAndGroupId(Long userId, Long groupId);

    @Query("select distinct ug from GroupAndUser ga join UserGroup ug on ug.id = ga.group.id join User u on u.id = ga.user.id where u.id = :userId")
    List<UserGroup> findUserGroupLoginUserAssociated(@Param("userId") Long userId);
}
