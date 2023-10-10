package cmc.mellyserver.domain.memory;

import cmc.mellyserver.dbcore.memory.Memory;
import cmc.mellyserver.dbcore.memory.MemoryRepository;
import cmc.mellyserver.domain.memory.query.MemoryQueryRepository;
import cmc.mellyserver.support.exception.BusinessException;
import cmc.mellyserver.support.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.HashMap;

@Component
@RequiredArgsConstructor
public class MemoryReader {

    private final MemoryRepository memoryRepository;

    private final MemoryQueryRepository memoryQueryRepository;

    public Memory findById(Long memoryId) {
        return memoryRepository.findById(memoryId).orElseThrow(() -> new BusinessException(ErrorCode.NO_SUCH_MEMORY));
    }

    public HashMap<String, Long> countMemoryInPlace(Long userId, Long placeId) {
        return memoryQueryRepository.countMemoriesBelongToPlace(userId, placeId);
    }
}
