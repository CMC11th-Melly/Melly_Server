package cmc.mellyserver.domain.memory;


import cmc.mellyserver.dbcore.group.enums.GroupType;
import cmc.mellyserver.domain.memory.dto.response.MemoryListResponse;
import cmc.mellyserver.domain.memory.query.MemoryQueryRepository;
import cmc.mellyserver.domain.memory.query.dto.ImageDto;
import cmc.mellyserver.domain.memory.query.dto.MemoryDetailResponseDto;
import cmc.mellyserver.domain.memory.query.dto.MemoryResponseDto;
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


    @Cacheable(value = "memory-detail:memory-id", key = "#memoryId")
    @Transactional(readOnly = true)
    public MemoryDetailResponseDto findMemoryDetail(final Long memoryId) {

        MemoryDetailResponseDto memoryDetail = memoryQueryRepository.findMemoryDetail(memoryId);
        List<ImageDto> memoryImage = memoryQueryRepository.findMemoryImage(memoryDetail.getMemoryId());
        memoryDetail.setMemoryImages(memoryImage);
        memoryDetail.setKeyword(null);
        return memoryDetail;
    }


    @Transactional(readOnly = true)
    public MemoryListResponse findLoginUserWriteMemoryBelongToPlace(final Long lastId, final Pageable pageable, final Long userId, final Long placeId, final GroupType groupType) {
        Slice<MemoryResponseDto> memoryResponseDtos = memoryQueryRepository.searchMemoryUserCreatedForPlace(lastId, pageable, userId, placeId, groupType);

        List<MemoryResponseDto> contents = memoryResponseDtos.getContent();
        boolean next = memoryResponseDtos.hasNext();
        return MemoryListResponse.from(contents, next);
    }


    @Transactional(readOnly = true)
    public MemoryListResponse findOtherUserWriteMemoryBelongToPlace(final Long lastId, final Pageable pageable, final Long userId, final Long placeId, final GroupType groupType) {
        Slice<MemoryResponseDto> memoryResponseDtos = memoryQueryRepository.searchMemoryOtherCreate(lastId, pageable, userId, placeId, groupType);

        List<MemoryResponseDto> contents = memoryResponseDtos.getContent();
        boolean next = memoryResponseDtos.hasNext();
        return MemoryListResponse.from(contents, next);
    }

    @Transactional(readOnly = true)
    public MemoryListResponse findMyGroupMemberWriteMemoryBelongToPlace(final Long lastId, final Pageable pageable, final Long userId, final Long placeId, final GroupType groupType) {
        Slice<MemoryResponseDto> memoryResponseDtos = memoryQueryRepository.getMyGroupMemoryInPlace(lastId, pageable, userId, placeId, groupType);

        List<MemoryResponseDto> contents = memoryResponseDtos.getContent();
        boolean next = memoryResponseDtos.hasNext();
        return MemoryListResponse.from(contents, next);
    }

    @Transactional(readOnly = true)
    public MemoryListResponse findMemoriesLoginUserWrite(final Long lastId, final Pageable pageable, final Long userId, final GroupType groupType) {
        Slice<MemoryResponseDto> memoryResponseDtos = memoryQueryRepository.searchMemoryUserCreatedForMyPage(lastId, pageable, userId, groupType);

        List<MemoryResponseDto> contents = memoryResponseDtos.getContent();
        boolean next = memoryResponseDtos.hasNext();
        return MemoryListResponse.from(contents, next);
    }

    @Transactional(readOnly = true)
    public MemoryListResponse findMemoriesUsersBelongToMyGroupWrite(final Long lastId, final Pageable pageable, final Long groupId, final Long userId, final GroupType groupType) {
        Slice<MemoryResponseDto> memoryResponseDtos = memoryQueryRepository.getMyGroupMemory(lastId, pageable, groupId, groupType);

        List<MemoryResponseDto> contents = memoryResponseDtos.getContent();
        boolean next = memoryResponseDtos.hasNext();
        return MemoryListResponse.from(contents, next);
    }
}
