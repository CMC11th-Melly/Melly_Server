package cmc.mellyserver.domain.memory;

import cmc.mellyserver.dbcore.memory.Memory;
import cmc.mellyserver.dbcore.memory.MemoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class MemoryWriter {

    private final MemoryRepository memoryRepository;

    public Memory save(Memory memory) {
        return memoryRepository.save(memory);
    }
}
