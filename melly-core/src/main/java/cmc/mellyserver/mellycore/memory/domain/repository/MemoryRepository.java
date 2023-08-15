package cmc.mellyserver.mellycore.memory.domain.repository;

import cmc.mellyserver.mellycore.common.exception.BusinessException;
import cmc.mellyserver.mellycore.common.exception.ErrorCode;
import cmc.mellyserver.mellycore.memory.domain.Memory;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MemoryRepository extends JpaRepository<Memory, Long> {

    default Memory getById(final Long id) {
        return findById(id).orElseThrow(() -> new BusinessException(ErrorCode.NO_SUCH_MEMORY));
    }
}
