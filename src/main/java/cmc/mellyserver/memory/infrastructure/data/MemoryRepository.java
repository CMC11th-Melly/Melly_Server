package cmc.mellyserver.memory.infrastructure.data;

import cmc.mellyserver.memory.domain.Memory;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MemoryRepository extends JpaRepository<Memory,Long> {

    List<Memory> findMemoryByPlaceIdIn(List<Long> placeIds);
}
