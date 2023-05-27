package cmc.mellyserver.memory.presentation.dto.common;

import cmc.mellyserver.group.domain.UserGroup;
import cmc.mellyserver.memory.application.dto.response.FindPlaceInfoByMemoryNameResponseDto;
import cmc.mellyserver.memory.application.dto.response.GroupListForSaveMemoryResponseDto;
import cmc.mellyserver.memory.application.dto.response.MemoryImageDto;
import cmc.mellyserver.memory.application.dto.response.MemoryUpdateFormResponseDto;
import cmc.mellyserver.memory.domain.Memory;
import cmc.mellyserver.memory.domain.repository.dto.MemoryResponseDto;
import cmc.mellyserver.memory.presentation.dto.response.FindPlaceInfoByMemoryNameResponse;
import cmc.mellyserver.memory.presentation.dto.response.GroupListForSaveMemoryResponse;
import cmc.mellyserver.memory.presentation.dto.response.MemoryResponse;
import cmc.mellyserver.memory.presentation.dto.response.MemoryUpdateFormResponse;
import org.springframework.data.domain.Slice;

import java.util.List;
import java.util.stream.Collectors;

public class MemoryAssembler {

    public static List<GroupListForSaveMemoryResponse> groupListForSaveMemoryResponse(List<GroupListForSaveMemoryResponseDto> groupListForSaveMemoryResponseDtoList)
    {
       return groupListForSaveMemoryResponseDtoList.stream()
               .map(each ->
                       GroupListForSaveMemoryResponse.builder().groupId(each.getGroupId())
                               .groupName(each.getGroupName())
                               .groupType(each.getGroupType()).build())
               .collect(Collectors.toList());
    }

    public static Slice<MemoryResponse> memoryResponses(Slice<MemoryResponseDto> memoryResponseDtos)
    {
        return memoryResponseDtos.map(each -> MemoryResponse.builder().placeId(each.getPlaceId()).placeName(each.getPlaceName()).memoryId(each.getMemoryId()).imageDtos(each.getMemoryImages()).title(each.getTitle()).content(each.getContent()).groupType(each.getGroupType()).groupName(each.getGroupName()).stars(each.getStars()).keyword(each.getKeyword()).loginUserWrite(each.isLoginUserWrite()).visitedDate(each.getVisitedDate()).build());
    }

    public static MemoryResponse memoryResponse(MemoryResponseDto memoryResponseDto)
    {
        return MemoryResponse.builder()
                .placeId(memoryResponseDto.getPlaceId())
                .placeName(memoryResponseDto.getPlaceName())
                .memoryId(memoryResponseDto.getMemoryId())
                .imageDtos(memoryResponseDto.getMemoryImages())
                .title(memoryResponseDto.getTitle())
                .content(memoryResponseDto.getContent())
                .groupType(memoryResponseDto.getGroupType())
                .groupName(memoryResponseDto.getGroupName())
                .stars(memoryResponseDto.getStars())
                .keyword(memoryResponseDto.getKeyword())
                .loginUserWrite(memoryResponseDto.isLoginUserWrite())
                .visitedDate(memoryResponseDto.getVisitedDate())
                .build();
    }


    public static MemoryUpdateFormResponse memoryUpdateFormResponse(MemoryUpdateFormResponseDto memoryUpdateFormResponseDto)
    {
        return MemoryUpdateFormResponse.builder()
                .memoryImages(memoryUpdateFormResponseDto.getMemoryImages())
                .title(memoryUpdateFormResponseDto.getTitle())
                .content(memoryUpdateFormResponseDto.getContent())
                .userGroups(memoryUpdateFormResponseDto.getUserGroups())
                .star(memoryUpdateFormResponseDto.getStar())
                .keywords(memoryUpdateFormResponseDto.getKeywords())
                .build();
    }

    public static List<FindPlaceInfoByMemoryNameResponse> findPlaceInfoByMemoryNameResponses(List<FindPlaceInfoByMemoryNameResponseDto> findPlaceInfoByMemoryNameResponseDtos)
    {
        return findPlaceInfoByMemoryNameResponseDtos.stream().map(each -> FindPlaceInfoByMemoryNameResponse.builder().placeId(each.getPlaceId()).memoryName(each.getMemoryName()).build()).collect(Collectors.toList());
    }

    public static MemoryUpdateFormResponseDto memoryUpdateFormResponse(Memory memory, List<UserGroup> userGroups)
    {
        return new MemoryUpdateFormResponseDto(
                memory.getMemoryImages().stream().map(mi -> new MemoryImageDto(mi.getId(), mi.getImagePath())).collect(Collectors.toList()),
                memory.getTitle(),
                memory.getContent(),
                userGroups.stream().map(ug -> new GroupListForSaveMemoryResponseDto(ug.getId(), ug.getGroupName(), ug.getGroupType())).collect(Collectors.toList()),
                memory.getStars(),
                memory.getKeyword()
        );
    }

}
