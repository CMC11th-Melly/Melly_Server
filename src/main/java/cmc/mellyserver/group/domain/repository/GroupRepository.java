package cmc.mellyserver.group.domain.repository;

import cmc.mellyserver.group.domain.UserGroup;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Set;

public interface GroupRepository extends JpaRepository<UserGroup,Long> {
    List<UserGroup> findByIdIn(Set<Long> groupIds);
}
