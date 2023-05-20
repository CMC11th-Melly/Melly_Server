package cmc.mellyserver.memory.application;

import cmc.mellyserver.common.enums.GroupType;
import cmc.mellyserver.memory.application.dto.request.CreateMemoryRequestDto;
import cmc.mellyserver.memory.application.dto.request.UpdateMemoryRequestDto;
import cmc.mellyserver.memory.application.dto.response.GroupListForSaveMemoryResponseDto;
import cmc.mellyserver.memory.application.dto.response.MemoryUpdateFormResponseDto;
import cmc.mellyserver.memory.domain.repository.dto.MemoryResponseDto;
import cmc.mellyserver.memory.application.dto.response.FindPlaceInfoByMemoryNameResponseDto;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import java.util.List;


public interface MemoryService {

    void createMemory(CreateMemoryRequestDto createMemoryRequestDto);

    void removeMemory(Long memoryId);

    void updateMemory(UpdateMemoryRequestDto updateMemoryRequestDto);

    List<GroupListForSaveMemoryResponseDto> findGroupListLoginUserParticipate(Long loginUserSeq);

    MemoryResponseDto findMemoryByMemoryId(Long loginUserSeq, Long memoryId);

    Slice<MemoryResponseDto> findLoginUserWriteMemoryBelongToPlace(Pageable pageable, Long loginUserSeq, Long placeId, GroupType groupType);

    Slice<MemoryResponseDto> findOtherUserWriteMemoryBelongToPlace(Pageable pageable, Long loginUserSeq, Long placeId, GroupType groupType);

    Slice<MemoryResponseDto> findMyGroupMemberWriteMemoryBelongToPlace(Pageable pageable, Long loginUserSeq, Long placeId, GroupType groupType);

    MemoryUpdateFormResponseDto findMemoryUpdateFormData(Long loginUserSeq, Long memoryId);

    List<FindPlaceInfoByMemoryNameResponseDto> findPlaceInfoByMemoryName(Long loginUserSeq, String memoryName);
}
