package cmc.mellyserver.mellycore.memory.application;

import cmc.mellyserver.mellycommon.enums.GroupType;
import cmc.mellyserver.mellycore.group.domain.UserGroup;
import cmc.mellyserver.mellycore.group.domain.repository.GroupAndUserRepository;
import cmc.mellyserver.mellycore.memory.application.dto.response.MemoryUpdateFormResponseDto;
import cmc.mellyserver.mellycore.memory.domain.Memory;
import cmc.mellyserver.mellycore.memory.domain.repository.MemoryQueryRepository;
import cmc.mellyserver.mellycore.memory.domain.repository.MemoryRepository;
import cmc.mellyserver.mellycore.memory.domain.repository.dto.MemoryResponseDto;
import cmc.mellyserver.mellycore.memory.exception.MemoryNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class MemoryReadService {

    private final MemoryQueryRepository memoryQueryRepository;

    private final MemoryRepository memoryRepository;

    private final GroupAndUserRepository groupAndUserRepository;


    @Cacheable(value = "memory", key = "#memoryId", cacheManager = "redisCacheManager")
    @Transactional(readOnly = true)
    public MemoryResponseDto findMemoryByMemoryId(Long userSeq, Long memoryId) {
        return memoryQueryRepository.getMemoryByMemoryId(userSeq, memoryId);
    }

    @Transactional(readOnly = true)
    public Slice<MemoryResponseDto> findLoginUserWriteMemoryBelongToPlace(Pageable pageable, Long userSeq, Long placeId, GroupType groupType) {
        return memoryQueryRepository.searchMemoryUserCreatedForPlace(pageable, userSeq, placeId, groupType);
    }

    @Transactional(readOnly = true)
    public Slice<MemoryResponseDto> findOtherUserWriteMemoryBelongToPlace(Pageable pageable, Long userSeq, Long placeId, GroupType groupType) {
        return memoryQueryRepository.searchMemoryOtherCreate(pageable, userSeq, placeId, groupType);
    }

    @Transactional(readOnly = true)
    public Slice<MemoryResponseDto> findMyGroupMemberWriteMemoryBelongToPlace(Pageable pageable, Long userSeq, Long placeId, GroupType groupType) {
        return memoryQueryRepository.getMyGroupMemory(pageable, userSeq, placeId, groupType);
    }

    @Transactional(readOnly = true)
    public Slice<MemoryResponseDto> findMemoriesLoginUserWrite(Pageable pageable, Long userSeq, GroupType groupType) {
        return memoryQueryRepository.searchMemoryUserCreatedForMyPage(pageable, userSeq, groupType);
    }

    @Transactional(readOnly = true)
    public Slice<MemoryResponseDto> findMemoriesUsersBelongToMyGroupWrite(Pageable pageable, Long groupId, Long userSeq) {
        return memoryQueryRepository.getMyGroupMemory(pageable, groupId, userSeq);
    }

    @Transactional(readOnly = true)
    public MemoryUpdateFormResponseDto findMemoryUpdateFormData(Long userSeq, Long memoryId) {

        Memory memory = memoryRepository.findById(memoryId).orElseThrow(() -> {
            throw new MemoryNotFoundException();
        });
        List<UserGroup> userGroupLoginUserAssociated = groupAndUserRepository.findUserGroupLoginUserAssociated(userSeq);
        return MemoryUpdateFormResponseDto.of(memory, userGroupLoginUserAssociated);
    }
}
