package cmc.mellyserver.domain.memory;

import java.util.HashMap;
import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Component;

import cmc.mellyserver.dbcore.group.GroupType;
import cmc.mellyserver.dbcore.group.UserGroup;
import cmc.mellyserver.dbcore.memory.keyword.Keyword;
import cmc.mellyserver.dbcore.memory.memory.Memory;
import cmc.mellyserver.dbcore.memory.memory.MemoryRepository;
import cmc.mellyserver.dbcore.place.Place;
import cmc.mellyserver.domain.group.GroupReader;
import cmc.mellyserver.domain.memory.dto.response.MemoryListResponse;
import cmc.mellyserver.domain.memory.keyword.KeywordReader;
import cmc.mellyserver.domain.memory.query.MemoryQueryRepository;
import cmc.mellyserver.domain.memory.query.dto.MemoryListResponseDto;
import cmc.mellyserver.domain.memory.query.dto.MemoryResponseDto;
import cmc.mellyserver.domain.place.PlaceReader;
import cmc.mellyserver.support.exception.BusinessException;
import cmc.mellyserver.support.exception.ErrorCode;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class MemoryReader {

    private final MemoryRepository memoryRepository;

    private final KeywordReader keywordReader;

    private final PlaceReader placeReader;

    private final GroupReader groupReader;

    private final MemoryQueryRepository memoryQueryRepository;

    public Memory findById(Long memoryId) {
        return memoryRepository.findById(memoryId).orElseThrow(() -> new BusinessException(ErrorCode.NO_SUCH_MEMORY));
    }

    public HashMap<String, Long> countMemoryInPlace(Long userId, Long placeId) {
        return memoryQueryRepository.countMemoriesBelongToPlace(userId, placeId);
    }

    public MemoryResponseDto getMemory(final Long memoryId) {

        Memory memory = memoryRepository.findById(memoryId)
            .orElseThrow(() -> new BusinessException(ErrorCode.NO_SUCH_MEMORY));
        List<Keyword> keywords = keywordReader.getKeywords(memory.getKeywordIds());
        Place place = placeReader.findById(memory.getPlaceId());

        if (memory.getGroupId().equals(-1L)) {
            return MemoryResponseDto.of(place, memory, keywords, UserGroup.builder().build());
        }

        UserGroup group = groupReader.findById(memory.getGroupId());
        return MemoryResponseDto.of(place, memory, keywords, group);
    }

    public MemoryListResponse getUserMemories(final Long lastId, final Pageable pageable, final Long userId,
        final Long placeId, final GroupType groupType) {
        Slice<MemoryListResponseDto> memoryResponseDtos = memoryQueryRepository.findUserMemories(lastId, pageable,
            userId,
            placeId, groupType);
        return transferToList(memoryResponseDtos);
    }

    public MemoryListResponse findOtherMemories(final Long lastId, final Pageable pageable, final Long userId,
        final Long placeId, final GroupType groupType) {
        Slice<MemoryListResponseDto> memoryResponseDtos = memoryQueryRepository.findOtherMemories(lastId, pageable,
            userId,
            placeId, groupType);
        return transferToList(memoryResponseDtos);
    }

    public MemoryListResponse findGroupMemoriesById(final Long lastId, final Pageable pageable, final Long groupId) {
        Slice<MemoryListResponseDto> memoryResponseDtos = memoryQueryRepository.findGroupMemoriesById(lastId, pageable,
            groupId);
        return transferToList(memoryResponseDtos);
    }

    public MemoryListResponse findGroupMemories(final Long lastId, final Pageable pageable, final Long userId,
        final Long placeId) {
        Slice<MemoryListResponseDto> groupMemories = memoryQueryRepository.findGroupMemories(lastId, pageable, userId,
            placeId);
        return transferToList(groupMemories);
    }

    private MemoryListResponse transferToList(Slice<MemoryListResponseDto> memoryResponseDtos) {
        List<MemoryListResponseDto> contents = memoryResponseDtos.getContent();
        boolean next = memoryResponseDtos.hasNext();
        return MemoryListResponse.from(contents, next);
    }

}
