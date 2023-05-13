package cmc.mellyserver.memory.infrastructure.data;

import cmc.mellyserver.common.enums.GroupType;
import cmc.mellyserver.memory.domain.Memory;
import cmc.mellyserver.memory.domain.repository.MemoryRepository;
import cmc.mellyserver.memory.infrastructure.data.dto.MemoryResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Component;

/**
 * MemoryRepositoryImpl.java
 *
 * @author jemlog
 */

@Component
@RequiredArgsConstructor
public class MemoryRepositoryImpl implements MemoryRepository {

    private final MemoryJpaRepository memoryJpaRepository;
    private final MemoryQueryRepository memoryQueryRepository;


    @Override
    public MemoryResponseDto findLoginUserCreatedMemoryById(Long loginUserSeq, Long memoryId) {
        return null;
    }

    @Override
    public Slice<MemoryResponseDto> findMemoriesLoginUserCreatedInPlace(Pageable pageable, Long loginUserSeq, Long placeId, GroupType groupType) {
        return null;
    }

    @Override
    public Slice<MemoryResponseDto> findMemoriesOtherUserCreatedInPlace(Pageable pageable, Long loginUserSeq, Long placeId, GroupType groupType) {
        return null;
    }

    @Override
    public Slice<MemoryResponseDto> findMemoriesMyGroupUsersCreatedInPlace(Pageable pageable, Long userSeq, Long placeId, GroupType groupType) {
        return null;
    }

    @Override
    public Memory createMemory(Memory memory) {
        return memoryJpaRepository.save(memory);
    }


}
