package cmc.mellyserver.block.memoryBlock.domain;

import cmc.mellyserver.report.memoryReport.domain.MemoryReport;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MemoryBlockRepository extends JpaRepository<MemoryBlock,Long> {
}
