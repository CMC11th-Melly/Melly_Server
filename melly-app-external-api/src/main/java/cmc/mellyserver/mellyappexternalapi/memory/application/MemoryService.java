package cmc.mellyserver.mellyappexternalapi.memory.application;

import cmc.mellyserver.mellyappexternalapi.memory.application.dto.request.CreateMemoryRequestDto;
import cmc.mellyserver.mellyappexternalapi.memory.application.dto.request.UpdateMemoryRequestDto;
import cmc.mellyserver.mellyappexternalapi.memory.application.dto.response.MemoryUpdateFormResponseDto;
import cmc.mellyserver.mellydomain.common.enums.GroupType;
import cmc.mellyserver.mellydomain.group.domain.repository.dto.GroupListForSaveMemoryResponseDto;
import cmc.mellyserver.mellydomain.memory.domain.Memory;
import cmc.mellyserver.mellydomain.memory.domain.repository.dto.FindPlaceInfoByMemoryNameResponseDto;
import cmc.mellyserver.mellydomain.memory.domain.repository.dto.MemoryResponseDto;
import java.util.List;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;

public interface MemoryService {

    Memory createMemory(CreateMemoryRequestDto createMemoryRequestDto);

    void removeMemory(Long loginUserSeq, Long memoryId);

    void updateMemory(UpdateMemoryRequestDto updateMemoryRequestDto);

    List<GroupListForSaveMemoryResponseDto> findGroupListLoginUserParticipate(Long loginUserSeq);

    MemoryResponseDto findMemoryByMemoryId(Long loginUserSeq, Long memoryId);

    Slice<MemoryResponseDto> findLoginUserWriteMemoryBelongToPlace(Pageable pageable,
            Long loginUserSeq, Long placeId,
            GroupType groupType);

    Slice<MemoryResponseDto> findOtherUserWriteMemoryBelongToPlace(Pageable pageable,
            Long loginUserSeq, Long placeId,
            GroupType groupType);

    Slice<MemoryResponseDto> findMyGroupMemberWriteMemoryBelongToPlace(Pageable pageable,
            Long loginUserSeq,
            Long placeId, GroupType groupType);

    MemoryUpdateFormResponseDto findMemoryUpdateFormData(Long loginUserSeq, Long memoryId);

    List<FindPlaceInfoByMemoryNameResponseDto> findPlaceInfoByMemoryName(Long loginUserSeq,
            String memoryName);
}
