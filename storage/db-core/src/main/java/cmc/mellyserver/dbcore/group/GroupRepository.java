package cmc.mellyserver.dbcore.group;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import jakarta.persistence.LockModeType;

public interface GroupRepository extends JpaRepository<UserGroup, Long> {

    /**
     * 조회한 뒤 무조건 버전을 하나 증가시키는 방식
     * 애그리거트에서 연관 엔티티가 변경됐을때, 루트에도 변경사항을 반영하기 위함
     */
    @Lock(LockModeType.OPTIMISTIC_FORCE_INCREMENT)
    @Query("select c from UserGroup c where c.id = :groupId")
    UserGroup findByOptimisticLock(@Param("groupId") Long groupId);
}
