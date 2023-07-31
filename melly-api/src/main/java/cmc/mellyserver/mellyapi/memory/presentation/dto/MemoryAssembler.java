package cmc.mellyserver.mellyapi.memory.presentation.dto;

import cmc.mellyserver.mellyapi.memory.presentation.dto.request.MemoryCreateRequest;
import cmc.mellyserver.mellyapi.memory.presentation.dto.request.MemoryUpdateRequest;
import cmc.mellyserver.mellyapi.memory.presentation.dto.response.FindPlaceInfoByMemoryNameResponse;
import cmc.mellyserver.mellyapi.memory.presentation.dto.response.GroupListForSaveMemoryResponse;
import cmc.mellyserver.mellyapi.memory.presentation.dto.response.MemoryResponse;
import cmc.mellyserver.mellyapi.memory.presentation.dto.response.MemoryUpdateFormResponse;
import cmc.mellyserver.mellycore.group.domain.repository.dto.GroupLoginUserParticipatedResponseDto;
import cmc.mellyserver.mellycore.memory.application.dto.request.CreateMemoryRequestDto;
import cmc.mellyserver.mellycore.memory.application.dto.request.UpdateMemoryRequestDto;
import cmc.mellyserver.mellycore.memory.application.dto.response.MemoryUpdateFormResponseDto;
import cmc.mellyserver.mellycore.memory.domain.repository.dto.FindPlaceInfoByMemoryNameResponseDto;
import cmc.mellyserver.mellycore.memory.domain.repository.dto.MemoryResponseDto;
import org.springframework.data.domain.Slice;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.stream.Collectors;

public abstract class MemoryAssembler {

    public static Slice<GroupListForSaveMemoryResponse> groupListForSaveMemoryResponse(
            Slice<GroupLoginUserParticipatedResponseDto> groupLoginUserParticipatedResponseDtos) {
        return groupLoginUserParticipatedResponseDtos
                .map(GroupListForSaveMemoryResponse::of);
    }

    public static Slice<MemoryResponse> memoryResponses(
            Slice<MemoryResponseDto> memoryResponseDtos) {
        return memoryResponseDtos.map(MemoryResponse::of);
    }

    public static MemoryUpdateFormResponse memoryUpdateFormResponse(
            MemoryUpdateFormResponseDto memoryUpdateFormResponseDto) {
        return MemoryUpdateFormResponse.builder()
                .memoryImages(memoryUpdateFormResponseDto.getMemoryImages())
                .title(memoryUpdateFormResponseDto.getTitle())
                .content(memoryUpdateFormResponseDto.getContent())
                .userGroups(memoryUpdateFormResponseDto.getUserGroups())
                .star(memoryUpdateFormResponseDto.getStar())
                .keywords(memoryUpdateFormResponseDto.getKeywords())
                .build();
    }

    public static List<FindPlaceInfoByMemoryNameResponse> findPlaceInfoByMemoryNameResponses(
            List<FindPlaceInfoByMemoryNameResponseDto> findPlaceInfoByMemoryNameResponseDtos) {
        return findPlaceInfoByMemoryNameResponseDtos.stream()
                .map(each -> FindPlaceInfoByMemoryNameResponse.builder()
                        .placeId(each.getPlaceId())
                        .memoryName(each.getMemoryName())
                        .build())
                .collect(Collectors.toList());
    }

    public static UpdateMemoryRequestDto updateMemoryRequestDto(Long id, Long memoryId,
                                                                MemoryUpdateRequest memoryUpdateRequest, List<MultipartFile> images) {
        return UpdateMemoryRequestDto.builder()
                .id(id)
                .memoryId(memoryId)
                .title(memoryUpdateRequest.getTitle())
                .content(memoryUpdateRequest.getContent())
                .keyword(memoryUpdateRequest.getKeyword())
                .groupId(memoryUpdateRequest.getGroupId())
                .openType(memoryUpdateRequest.getOpenType())
                .visitedDate(memoryUpdateRequest.getVisitedDate())
                .star(memoryUpdateRequest.getStar())
                .deleteImageList(memoryUpdateRequest.getDeleteImageList())
                .images(images)
                .build();
    }

    public static CreateMemoryRequestDto createMemoryRequestDto(Long id,
                                                                List<MultipartFile> images,
                                                                MemoryCreateRequest placeInfoRequest) {
        return CreateMemoryRequestDto.builder()
                .id(id)
                .lat(placeInfoRequest.getLat())
                .lng(placeInfoRequest.getLng())
                .title(placeInfoRequest.getTitle())
                .placeName(placeInfoRequest.getPlaceName())
                .placeName(placeInfoRequest.getPlaceCategory())
                .content(placeInfoRequest.getContent())
                .star(placeInfoRequest.getStar())
                .groupId(placeInfoRequest.getGroupId())
                .openType(placeInfoRequest.getOpenType())
                .keyword(placeInfoRequest.getKeyword())
                .visitedDate(placeInfoRequest.getVisitedDate())
                .multipartFiles(images)
                .build();
    }

}
