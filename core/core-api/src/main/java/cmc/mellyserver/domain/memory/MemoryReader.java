package cmc.mellyserver.domain.memory;

import java.util.HashMap;

import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

import cmc.mellyserver.dbcore.group.GroupType;
import cmc.mellyserver.dbcore.memory.memory.Memory;
import cmc.mellyserver.dbcore.memory.memory.MemoryRepository;
import cmc.mellyserver.domain.memory.dto.response.MemoryListResponse;
import cmc.mellyserver.domain.memory.query.MemoryQueryRepository;
import cmc.mellyserver.support.exception.BusinessException;
import cmc.mellyserver.support.exception.ErrorCode;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class MemoryReader {

    private final MemoryRepository memoryRepository;

    private final MemoryQueryRepository memoryQueryRepository;

    public Memory read(Long memoryId) {
        return memoryRepository.findById(memoryId).orElseThrow(() -> new BusinessException(ErrorCode.NO_SUCH_MEMORY));
    }

    public HashMap<String, Long> countMemoryInPlace(Long userId, Long placeId) {
        return memoryQueryRepository.countMemoriesBelongToPlace(userId, placeId);
    }

    public MemoryListResponse getUserMemories(Long lastId, Long userId, Long placeId, GroupType groupType,
        Pageable pageable) {
        return memoryQueryRepository.findUserMemories(lastId, pageable, userId, placeId, groupType);
    }

    public MemoryListResponse findOtherMemories(Long lastId, Long userId, Long placeId, GroupType groupType,
        Pageable pageable) {
        return memoryQueryRepository.findOtherMemories(lastId, pageable, userId, placeId, groupType);

    }

    public MemoryListResponse findGroupMemoriesById(Long lastId, Long groupId, Pageable pageable) {
        return memoryQueryRepository.findGroupMemoriesById(lastId, pageable, groupId);
    }

    public MemoryListResponse findGroupMemories(Long lastId, Long userId, Long placeId, Pageable pageable) {
        return memoryQueryRepository.findGroupMemories(lastId, pageable, userId, placeId);
    }
}
