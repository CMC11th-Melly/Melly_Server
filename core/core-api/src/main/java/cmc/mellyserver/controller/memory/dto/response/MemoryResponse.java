package cmc.mellyserver.controller.memory.dto.response;

import java.time.LocalDate;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonFormat;

import cmc.mellyserver.dbcore.group.GroupType;
import cmc.mellyserver.domain.memory.query.dto.MemoryImageDto;
import cmc.mellyserver.domain.memory.query.dto.MemoryResponseDto;
import lombok.Builder;

public record MemoryResponse(
    Long placeId,
    String placeName,

    Long memoryId,
    String title,
    String content,
    List<MemoryImageDto> memoryImages,
    List<String> keywords,

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    LocalDate visitedDate,
    long stars,

    Long groupId,
    GroupType groupType,
    String groupName,
    int groupIcon
) {

    @Builder
    public MemoryResponse {
    }

    public static MemoryResponse of(MemoryResponseDto memoryResponseDto) {
        return MemoryResponse.builder()
            .placeId(memoryResponseDto.placeId())
            .placeName(memoryResponseDto.placeName())
            .memoryId(memoryResponseDto.memoryId())
            .title(memoryResponseDto.title())
            .content(memoryResponseDto.content())
            .memoryImages(memoryResponseDto.memoryImages())
            .visitedDate(memoryResponseDto.visitedDate())
            .stars(memoryResponseDto.stars())
            .keywords(memoryResponseDto.keywords())
            .groupId(memoryResponseDto.groupId())
            .groupType(memoryResponseDto.groupType())
            .groupName(memoryResponseDto.groupName())
            .groupIcon(memoryResponseDto.groupIcon())
            .build();
    }
}
