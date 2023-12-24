package cmc.mellyserver.dbcore.group;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import jakarta.persistence.LockModeType;

public interface GroupRepository extends JpaRepository<UserGroup, Long> {

    @Lock(LockModeType.OPTIMISTIC_FORCE_INCREMENT)
    @Query("select c from UserGroup c where c.id = :groupId")
    UserGroup findByOptimisticLock(@Param("groupId") Long groupId);
}
