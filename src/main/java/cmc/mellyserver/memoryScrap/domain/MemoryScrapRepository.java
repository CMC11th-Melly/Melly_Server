package cmc.mellyserver.memoryScrap.domain;

import cmc.mellyserver.memory.domain.Memory;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface MemoryScrapRepository extends JpaRepository<MemoryScrap,Long> {

    Optional<MemoryScrap> findMemoryScrapByUserUserSeqAndMemory(Long userSeq, Memory memory);
    void deleteByUserUserSeqAndMemory(Long userSeq, Memory memory);
}
