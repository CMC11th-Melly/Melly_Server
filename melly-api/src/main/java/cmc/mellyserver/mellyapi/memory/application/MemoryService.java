package cmc.mellyserver.mellyapi.memory.application;

import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;

import cmc.mellyserver.mellyapi.memory.application.dto.request.CreateMemoryRequestDto;
import cmc.mellyserver.mellyapi.memory.application.dto.request.UpdateMemoryRequestDto;
import cmc.mellyserver.mellyapi.memory.application.dto.response.MemoryUpdateFormResponseDto;
import cmc.mellyserver.mellycore.common.enums.GroupType;
import cmc.mellyserver.mellycore.group.domain.repository.dto.GroupListForSaveMemoryResponseDto;
import cmc.mellyserver.mellycore.memory.domain.repository.dto.FindPlaceInfoByMemoryNameResponseDto;
import cmc.mellyserver.mellycore.memory.domain.repository.dto.MemoryResponseDto;

public interface MemoryService {

	void createMemory(CreateMemoryRequestDto createMemoryRequestDto);

	void removeMemory(Long memoryId);

	void updateMemory(UpdateMemoryRequestDto updateMemoryRequestDto);

	List<GroupListForSaveMemoryResponseDto> findGroupListLoginUserParticipate(Long loginUserSeq);

	MemoryResponseDto findMemoryByMemoryId(Long loginUserSeq, Long memoryId);

	Slice<MemoryResponseDto> findLoginUserWriteMemoryBelongToPlace(Pageable pageable, Long loginUserSeq, Long placeId,
		GroupType groupType);

	Slice<MemoryResponseDto> findOtherUserWriteMemoryBelongToPlace(Pageable pageable, Long loginUserSeq, Long placeId,
		GroupType groupType);

	Slice<MemoryResponseDto> findMyGroupMemberWriteMemoryBelongToPlace(Pageable pageable, Long loginUserSeq,
		Long placeId, GroupType groupType);

	MemoryUpdateFormResponseDto findMemoryUpdateFormData(Long loginUserSeq, Long memoryId);

	List<FindPlaceInfoByMemoryNameResponseDto> findPlaceInfoByMemoryName(Long loginUserSeq, String memoryName);
}
