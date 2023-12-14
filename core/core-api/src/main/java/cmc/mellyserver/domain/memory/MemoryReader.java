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

    public MemoryListResponse getUserMemories(final Long lastId, final Pageable pageable, final Long userId,
        final Long placeId, final GroupType groupType) {
        return memoryQueryRepository.findUserMemories(lastId, pageable, userId, placeId, groupType);
    }

    public MemoryListResponse findOtherMemories(final Long lastId, final Pageable pageable, final Long userId,
        final Long placeId, final GroupType groupType) {
        return memoryQueryRepository.findOtherMemories(lastId, pageable, userId, placeId, groupType);

    }

    public MemoryListResponse findGroupMemoriesById(final Long lastId, final Pageable pageable, final Long groupId) {
        return memoryQueryRepository.findGroupMemoriesById(lastId, pageable, groupId);
    }

    public MemoryListResponse findGroupMemories(final Long lastId, final Pageable pageable, final Long userId,
        final Long placeId) {
        return memoryQueryRepository.findGroupMemories(lastId, pageable, userId, placeId);
    }
}
