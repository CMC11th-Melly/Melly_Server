package cmc.mellyserver.controller.memory.dto;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.web.multipart.MultipartFile;

import cmc.mellyserver.controller.memory.dto.request.MemoryCreateRequest;
import cmc.mellyserver.controller.memory.dto.request.MemoryUpdateRequest;
import cmc.mellyserver.controller.memory.dto.response.FindPlaceInfoByMemoryNameResponse;
import cmc.mellyserver.domain.memory.dto.request.CreateMemoryRequestDto;
import cmc.mellyserver.domain.memory.dto.request.UpdateMemoryRequestDto;
import cmc.mellyserver.domain.memory.query.dto.FindPlaceByMemoryTitleResponseDto;

public abstract class MemoryAssembler {

    public static List<FindPlaceInfoByMemoryNameResponse> findPlaceInfoByMemoryNameResponses(
        List<FindPlaceByMemoryTitleResponseDto> findPlaceInfoByMemoryNameResponseDtos) {
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

    public static CreateMemoryRequestDto createMemoryRequestDto(Long id, List<MultipartFile> images,
        MemoryCreateRequest memoryCreateRequest) {
        return CreateMemoryRequestDto.builder()
            .userId(id)
            .lat(memoryCreateRequest.getLat())
            .lng(memoryCreateRequest.getLng())
            .title(memoryCreateRequest.getTitle())
            .placeName(memoryCreateRequest.getPlaceName())
            .placeName(memoryCreateRequest.getPlaceCategory())
            .content(memoryCreateRequest.getContent())
            .star(memoryCreateRequest.getStar())
            .keywordIds(memoryCreateRequest.getKeywordIds())
            .groupId(memoryCreateRequest.getGroupId())
            .openType(memoryCreateRequest.getOpenType())
            .visitedDate(memoryCreateRequest.getVisitedDate())
            .multipartFiles(images)
            .build();
    }

}
