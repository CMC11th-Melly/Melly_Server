package cmc.mellyserver.mellycore.memory.domain.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import cmc.mellyserver.mellycore.memory.domain.Memory;

public interface MemoryRepository extends JpaRepository<Memory, Long> {

	List<Memory> findMemoryByPlaceIdIn(List<Long> placeIds);
}
