package cmc.mellyserver.dbcore.group;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface GroupAndUserRepository extends JpaRepository<GroupAndUser, Long> {

    Optional<GroupAndUser> findByUserIdAndGroupId(Long userId, Long groupId);

    @Query("select count(*) from GroupAndUser ga where ga.group.id = :groupId")
    int countUserParticipatedInGroup(@Param("groupId") Long groupId);

    @Query("select ga.userId from GroupAndUser ga where ga.group.id = :groupId")
    List<Long> getJoinedUserIds(@Param("groupId") Long groupId);

    void deleteByUserIdAndGroupId(Long userId, Long groupId);
}
