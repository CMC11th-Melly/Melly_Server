package cmc.mellyserver.mellycore.group.domain.repository;

import java.util.List;
import java.util.Set;

import org.springframework.data.jpa.repository.JpaRepository;

import cmc.mellyserver.mellycore.group.domain.UserGroup;

public interface GroupRepository extends JpaRepository<UserGroup, Long> {
	List<UserGroup> findByIdIn(Set<Long> groupIds);
}
