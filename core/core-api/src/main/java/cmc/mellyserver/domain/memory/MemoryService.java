package cmc.mellyserver.domain.memory;

import java.util.List;

import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cmc.mellyserver.config.cache.CacheNames;
import cmc.mellyserver.dbcore.group.GroupType;
import cmc.mellyserver.dbcore.group.UserGroup;
import cmc.mellyserver.dbcore.memory.keyword.Keyword;
import cmc.mellyserver.dbcore.memory.memory.Memory;
import cmc.mellyserver.dbcore.place.Place;
import cmc.mellyserver.domain.group.GroupReader;
import cmc.mellyserver.domain.memory.dto.request.CreateMemoryRequestDto;
import cmc.mellyserver.domain.memory.dto.request.UpdateMemoryRequestDto;
import cmc.mellyserver.domain.memory.dto.response.MemoryListResponse;
import cmc.mellyserver.domain.memory.event.MemoryCreatedEvent;
import cmc.mellyserver.domain.memory.keyword.KeywordReader;
import cmc.mellyserver.domain.memory.query.dto.MemoryResponseDto;
import cmc.mellyserver.domain.place.PlaceReader;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class MemoryService {

    private final MemoryReader memoryReader;

    private final MemoryWriter memoryWriter;

    private final KeywordReader keywordReader;

    private final PlaceReader placeReader;

    private final GroupReader groupReader;

    private final ApplicationEventPublisher eventPublisher;

    /*
    메모리 상세 정보 조회
     */
    @Cacheable(cacheNames = CacheNames.DETAIL_MEMORY, key = "#memoryId")
    public MemoryResponseDto getMemory(final Long memoryId) {

        Memory memory = memoryReader.read(memoryId);
        List<Keyword> keywords = keywordReader.read(memory.getKeywordIds());
        Place place = placeReader.read(memory.getPlaceId());
        UserGroup userGroup = groupReader.readOrDefaultEmpty(memory.getGroupId());
        return MemoryResponseDto.of(place, memory, keywords, userGroup);
    }

    /*
    특정 장소에 내가 작성한 메모리 조회
     */
    public MemoryListResponse getUserMemoriesInPlace(Long lastId, Long userId, Long placeId, GroupType groupType,
        Pageable pageable) {
        return memoryReader.getUserMemories(lastId, userId, placeId, groupType, pageable);
    }

    /*
    특정 장소에 나 이외의 사람이 작성한 메모리 조회
     */
    public MemoryListResponse getOtherMemoriesInPlace(Long lastId, Long userId, Long placeId, GroupType groupType,
        Pageable pageable) {
        return memoryReader.findOtherMemories(lastId, userId, placeId, groupType, pageable);
    }

    /*
    특정 장소에 우리 그룹 사람들이 작성한 메모리 조회
     */
    public MemoryListResponse getGroupMemoriesInPlace(Long lastId, Long userId, Long placeId, Pageable pageable) {
        return memoryReader.findGroupMemories(lastId, userId, placeId, pageable);
    }

    /*
    유저가 작성한 메모리 조회
    */
    public MemoryListResponse getUserMemories(Long lastId, Long userId, Long placeId, GroupType groupType,
        Pageable pageable) {
        return memoryReader.getUserMemories(lastId, userId, placeId, groupType, pageable);
    }

    /*
    내 그룹이 작성한 메모리 조회
     */
    public MemoryListResponse getGroupMemoriesById(Long lastId, Long groupId, Pageable pageable) {
        return memoryReader.findGroupMemoriesById(lastId, groupId, pageable);
    }

    @Transactional
    public void createMemory(CreateMemoryRequestDto createMemoryRequestDto) {
        Long memoryId = memoryWriter.save(createMemoryRequestDto);
        eventPublisher.publishEvent(new MemoryCreatedEvent(memoryId));
    }

    @CacheEvict(cacheNames = CacheNames.DETAIL_MEMORY, key = "#updateMemoryRequestDto.memoryId")
    @Transactional
    public void updateMemory(UpdateMemoryRequestDto updateMemoryRequestDto) {
        memoryWriter.update(updateMemoryRequestDto);
    }

    @CacheEvict(cacheNames = CacheNames.DETAIL_MEMORY, key = "#memoryId")
    @Transactional
    public void removeMemory(Long memoryId) {
        memoryWriter.remove(memoryId);
    }

}
