package cmc.mellyserver.domain.memory;

import cmc.mellyserver.dbcore.memory.MemoryRepository;
import cmc.mellyserver.domain.memory.query.MemoryQueryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.HashMap;

@Component
@RequiredArgsConstructor
public class MemoryReader {

    private final MemoryRepository memoryRepository;

    private final MemoryQueryRepository memoryQueryRepository;


    public HashMap<String, Long> countMemoryInPlace(Long userId, Long placeId) {
        return memoryQueryRepository.countMemoriesBelongToPlace(userId, placeId);
    }
}
