package cmc.mellyserver.domain.memory;

import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cmc.mellyserver.dbcore.group.enums.GroupType;
import cmc.mellyserver.domain.memory.dto.response.MemoryListResponse;
import cmc.mellyserver.domain.memory.query.dto.MemoryDetailResponseDto;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class MemoryReadService {

	private final MemoryReader memoryReader;

	/*
	메모리 상세 정보 조회
	 */
	@Cacheable(value = "memory-detail:memory-id", key = "#memoryId")
	public MemoryDetailResponseDto findMemoryDetail(final Long memoryId) {

		return memoryReader.findMemoryDetail(memoryId);
	}

	/*
	특정 장소에 내가 작성한 메모리 조회
	 */
	public MemoryListResponse findUserMemoriesInPlace(final Long lastId, final Pageable pageable, final Long userId,
		final Long placeId, final GroupType groupType) {

		return memoryReader.findUserMemories(lastId, pageable, userId, placeId, groupType);
	}

	/*
	특정 장소에 나 이외의 사람이 작성한 메모리 조회
	 */
	public MemoryListResponse findOtherMemoriesInPlace(final Long lastId, final Pageable pageable,
		final Long userId, final Long placeId, final GroupType groupType) {

		return memoryReader.findOtherMemories(lastId, pageable, userId, placeId, groupType);
	}

	/*
	특정 장소에 우리 그룹 사람들이 작성한 메모리 조회
	 */
	public MemoryListResponse findGroupMemoriesInPlace(final Long lastId, final Pageable pageable,
		final Long userId, final Long placeId, final GroupType groupType) {

		return memoryReader.findGroupMemories(lastId, pageable, userId, placeId, groupType);
	}

	/*
    유저가 작성한 메모리 조회
    */
	public MemoryListResponse findUserMemories(final Long lastId, final Pageable pageable, final Long userId,
		final Long placeId, final GroupType groupType) {
		return memoryReader.findUserMemories(lastId, pageable, userId, placeId, groupType);
	}

	/*
	내 그룹이 작성한 메모리 조회
	 */
	public MemoryListResponse findGroupMemoriesById(final Long lastId, final Pageable pageable, final Long groupId,
		final Long userId, final GroupType groupType) {
		return memoryReader.findGroupMemoriesById(lastId, pageable, groupId, userId, groupType);
	}
}
