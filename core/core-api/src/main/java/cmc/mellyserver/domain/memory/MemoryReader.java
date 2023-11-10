package cmc.mellyserver.domain.memory;

import java.util.HashMap;
import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Component;

import cmc.mellyserver.dbcore.group.enums.GroupType;
import cmc.mellyserver.dbcore.memory.Memory;
import cmc.mellyserver.dbcore.memory.MemoryRepository;
import cmc.mellyserver.domain.memory.dto.response.MemoryListResponse;
import cmc.mellyserver.domain.memory.query.MemoryQueryRepository;
import cmc.mellyserver.domain.memory.query.dto.ImageDto;
import cmc.mellyserver.domain.memory.query.dto.MemoryDetailResponseDto;
import cmc.mellyserver.domain.memory.query.dto.MemoryResponseDto;
import cmc.mellyserver.support.exception.BusinessException;
import cmc.mellyserver.support.exception.ErrorCode;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class MemoryReader {

	private final MemoryRepository memoryRepository;

	private final MemoryQueryRepository memoryQueryRepository;

	public Memory findById(Long memoryId) {
		return memoryRepository.findById(memoryId).orElseThrow(() -> new BusinessException(ErrorCode.NO_SUCH_MEMORY));
	}

	public HashMap<String, Long> countMemoryInPlace(Long userId, Long placeId) {
		return memoryQueryRepository.countMemoriesBelongToPlace(userId, placeId);
	}

	public MemoryDetailResponseDto getMemory(final Long memoryId) {
		MemoryDetailResponseDto memoryDetail = memoryQueryRepository.findMemoryDetail(memoryId);
		List<ImageDto> memoryImage = memoryQueryRepository.findMemoryImage(memoryDetail.getMemoryId());
		memoryDetail.setMemoryImages(memoryImage);
		memoryDetail.setKeyword(null);
		return memoryDetail;
	}

	public MemoryListResponse getUserMemories(final Long lastId, final Pageable pageable, final Long userId,
		final Long placeId, final GroupType groupType) {
		Slice<MemoryResponseDto> memoryResponseDtos = memoryQueryRepository.findUserMemories(lastId, pageable, userId,
			placeId, groupType);
		return transferToList(memoryResponseDtos);
	}

	public MemoryListResponse findOtherMemories(final Long lastId, final Pageable pageable, final Long userId,
		final Long placeId, final GroupType groupType) {
		Slice<MemoryResponseDto> memoryResponseDtos = memoryQueryRepository.findOtherMemories(lastId, pageable, userId,
			placeId, groupType);
		return transferToList(memoryResponseDtos);
	}

	public MemoryListResponse findGroupMemoriesById(final Long lastId, final Pageable pageable, final Long groupId,
		final Long userId, final GroupType groupType) {
		Slice<MemoryResponseDto> memoryResponseDtos = memoryQueryRepository.findGroupMemoriesById(lastId, pageable,
			groupId, userId, groupType);
		return transferToList(memoryResponseDtos);
	}

	public MemoryListResponse findGroupMemories(final Long lastId, final Pageable pageable, final Long userId,
		final Long placeId, final GroupType groupType) {
		Slice<MemoryResponseDto> groupMemories = memoryQueryRepository.findGroupMemories(lastId, pageable, userId,
			placeId, groupType);
		return transferToList(groupMemories);
	}

	private MemoryListResponse transferToList(Slice<MemoryResponseDto> memoryResponseDtos) {
		List<MemoryResponseDto> contents = memoryResponseDtos.getContent();
		boolean next = memoryResponseDtos.hasNext();
		return MemoryListResponse.from(contents, next);
	}

}
