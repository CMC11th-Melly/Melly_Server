package cmc.mellyserver.mellycore.memory.application;

import cmc.mellyserver.mellycore.common.exception.BusinessException;
import cmc.mellyserver.mellycore.common.exception.ErrorCode;
import cmc.mellyserver.mellycore.group.domain.UserGroup;
import cmc.mellyserver.mellycore.group.domain.repository.GroupAndUserRepository;
import cmc.mellyserver.mellycore.group.domain.enums.GroupType;
import cmc.mellyserver.mellycore.memory.application.dto.response.MemoryUpdateFormResponseDto;
import cmc.mellyserver.mellycore.memory.domain.Memory;
import cmc.mellyserver.mellycore.memory.domain.repository.MemoryQueryRepository;
import cmc.mellyserver.mellycore.memory.domain.repository.MemoryRepository;
import cmc.mellyserver.mellycore.memory.domain.repository.dto.ImageDto;
import cmc.mellyserver.mellycore.memory.domain.repository.dto.MemoryDetailResponseDto;
import cmc.mellyserver.mellycore.memory.domain.repository.dto.MemoryResponseDto;
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


    @Cacheable(value = "memory-detail:memory-id", key = "#memoryId", cacheManager = "redisCacheManager")
    @Transactional(readOnly = true)
    public MemoryDetailResponseDto findMemoryDetail(Long memoryId) {

        MemoryDetailResponseDto memoryDetail = memoryQueryRepository.findMemoryDetail(memoryId);
        List<ImageDto> memoryImage = memoryQueryRepository.findMemoryImage(memoryDetail.getMemoryId());
        memoryDetail.setMemoryImages(memoryImage);
        memoryDetail.setKeyword(null);
        return memoryDetail;
    }

    @Transactional(readOnly = true)
    public Slice<MemoryResponseDto> findLoginUserWriteMemoryBelongToPlace(Long lastId, Pageable pageable, Long userId, Long placeId, GroupType groupType) {
        return memoryQueryRepository.searchMemoryUserCreatedForPlace(lastId, pageable, userId, placeId, groupType);
    }

    @Transactional(readOnly = true)
    public Slice<MemoryResponseDto> findOtherUserWriteMemoryBelongToPlace(Long lastId, Pageable pageable, Long userId, Long placeId, GroupType groupType) {
        return memoryQueryRepository.searchMemoryOtherCreate(lastId, pageable, userId, placeId, groupType);
    }

    @Transactional(readOnly = true)
    public Slice<MemoryResponseDto> findMyGroupMemberWriteMemoryBelongToPlace(Long lastId, Pageable pageable, Long userId, Long placeId, GroupType groupType) {
        return memoryQueryRepository.getMyGroupMemoryInPlace(lastId, pageable, userId, placeId, groupType);
    }

    @Transactional(readOnly = true)
    public Slice<MemoryResponseDto> findMemoriesLoginUserWrite(Long lastId, Pageable pageable, Long userId, GroupType groupType) {
        return memoryQueryRepository.searchMemoryUserCreatedForMyPage(lastId, pageable, userId, groupType);
    }

    @Transactional(readOnly = true)
    public Slice<MemoryResponseDto> findMemoriesUsersBelongToMyGroupWrite(Long lastId, Pageable pageable, Long groupId, GroupType groupType) {
        return memoryQueryRepository.getMyGroupMemory(lastId, pageable, groupId, groupType);
    }

    @Transactional(readOnly = true)
    public MemoryUpdateFormResponseDto findMemoryUpdateFormData(Long id, Long memoryId) {

        Memory memory = memoryRepository.findById(memoryId).orElseThrow(() -> {
            throw new BusinessException(ErrorCode.NO_SUCH_MEMORY);
        });
        List<UserGroup> userGroupLoginUserAssociated = groupAndUserRepository.findUserGroupLoginUserAssociated(id);
        return MemoryUpdateFormResponseDto.of(memory, userGroupLoginUserAssociated);
    }
}
