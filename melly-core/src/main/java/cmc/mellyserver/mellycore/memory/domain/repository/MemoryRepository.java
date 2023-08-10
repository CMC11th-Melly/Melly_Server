package cmc.mellyserver.mellycore.memory.domain.repository;

import cmc.mellyserver.mellycore.memory.domain.Memory;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MemoryRepository extends JpaRepository<Memory, Long> {

}
