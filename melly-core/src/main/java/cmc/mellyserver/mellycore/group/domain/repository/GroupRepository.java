package cmc.mellyserver.mellycore.group.domain.repository;

import cmc.mellyserver.mellycore.common.exception.BusinessException;
import cmc.mellyserver.mellycore.common.exception.ErrorCode;
import cmc.mellyserver.mellycore.group.domain.UserGroup;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Set;

public interface GroupRepository extends JpaRepository<UserGroup, Long> {
	List<UserGroup> findByIdIn(Set<Long> groupIds);

	default UserGroup getById(Long groupId) {
		return findById(groupId).orElseThrow(() -> new BusinessException(ErrorCode.NO_SUCH_GROUP));
	}
}
