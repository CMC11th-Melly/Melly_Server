package cmc.mellyserver.memory.domain.repository;

import cmc.mellyserver.common.enums.GroupType;
import cmc.mellyserver.memory.domain.Memory;
import cmc.mellyserver.memory.infrastructure.data.dto.MemoryResponseDto;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;

import java.util.Optional;

/**
 * MemoryRepository.java
 *
 * @author jemlog
 */
public interface MemoryRepository {

    MemoryResponseDto findLoginUserCreatedMemoryById(Long loginUserSeq, Long memoryId);

    Slice<MemoryResponseDto> findMemoriesLoginUserCreatedInPlace(Pageable pageable, Long loginUserSeq, Long placeId, GroupType groupType);

    Slice<MemoryResponseDto> findMemoriesOtherUserCreatedInPlace(Pageable pageable, Long loginUserSeq, Long placeId, GroupType groupType);

    Slice<MemoryResponseDto> findMemoriesMyGroupUsersCreatedInPlace(Pageable pageable,Long userSeq, Long placeId,GroupType groupType);

    Memory createMemory(Memory memory);

    Optional<Memory> findMemoryById(Long memoryId);

}
