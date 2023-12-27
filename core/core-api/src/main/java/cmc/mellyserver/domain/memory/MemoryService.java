package cmc.mellyserver.domain.memory;

import java.util.List;

import org.springframework.cache.annotation.CacheEvict;
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
    TODO : 각 reader들이 MSA 상에서 client 통신을 통해 데이터를 조회하고, 복잡한 전시 서비스라 생각하고 캐시를 설계해보자
     */
    // @Cacheable(cacheNames = CacheNames.DETAIL_MEMORY, key = "#memoryId")
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
    public MemoryListResponse getUserMemoriesInPlace(final Long lastId, final Pageable pageable, final Long userId,
        final Long placeId, final GroupType groupType) {
        return memoryReader.getUserMemories(lastId, pageable, userId, placeId, groupType);
    }

    /*
    특정 장소에 나 이외의 사람이 작성한 메모리 조회
     */
    public MemoryListResponse getOtherMemoriesInPlace(final Long lastId, final Pageable pageable, final Long userId,
        final Long placeId, final GroupType groupType) {
        return memoryReader.findOtherMemories(lastId, pageable, userId, placeId, groupType);
    }

    /*
    특정 장소에 우리 그룹 사람들이 작성한 메모리 조회
     */
    public MemoryListResponse getGroupMemoriesInPlace(final Long lastId, final Pageable pageable, final Long userId,
        final Long placeId) {
        return memoryReader.findGroupMemories(lastId, pageable, userId, placeId);
    }

    /*
    유저가 작성한 메모리 조회
    */
    public MemoryListResponse getUserMemories(final Long lastId, final Pageable pageable, final Long userId,
        final Long placeId, final GroupType groupType) {
        return memoryReader.getUserMemories(lastId, pageable, userId, placeId, groupType);
    }

    /*
    내 그룹이 작성한 메모리 조회
     */
    public MemoryListResponse getGroupMemoriesById(final Long lastId, final Pageable pageable, final Long groupId) {
        return memoryReader.findGroupMemoriesById(lastId, pageable, groupId);
    }

    @Transactional
    public void createMemory(CreateMemoryRequestDto createMemoryRequestDto) {
        Memory memory = memoryWriter.save(createMemoryRequestDto);
        eventPublisher.publishEvent(new MemoryCreatedEvent(memory.getId()));
    }

    @CacheEvict(cacheNames = CacheNames.DETAIL_MEMORY, key = "#updateMemoryRequestDto.memoryId")
    @Transactional
    public void updateMemory(final UpdateMemoryRequestDto updateMemoryRequestDto) {
        memoryWriter.update(updateMemoryRequestDto);
    }

    @CacheEvict(cacheNames = CacheNames.DETAIL_MEMORY, key = "#memoryId")
    @Transactional
    public void removeMemory(final Long memoryId) {
        memoryWriter.remove(memoryId);
    }

}
