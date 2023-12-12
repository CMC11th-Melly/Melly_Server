package cmc.mellyserver.domain.memory;

import java.util.HashMap;
import java.util.List;

import org.springframework.data.domain.Pageable;
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
import cmc.mellyserver.domain.memory.query.dto.MemoryResponseDto;
import cmc.mellyserver.domain.place.PlaceReader;
import cmc.mellyserver.support.exception.BusinessException;
import cmc.mellyserver.support.exception.ErrorCode;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class MemoryReader {

    private static final Long EMPTY_GROUP = -1L;

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

        if (memory.getGroupId().equals(EMPTY_GROUP)) {
            return MemoryResponseDto.of(place, memory, keywords, UserGroup.builder().build());
        }

        UserGroup group = groupReader.findById(memory.getGroupId());
        return MemoryResponseDto.of(place, memory, keywords, group);
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
