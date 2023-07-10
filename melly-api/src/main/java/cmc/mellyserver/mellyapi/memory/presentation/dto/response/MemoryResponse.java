package cmc.mellyserver.mellyapi.memory.presentation.dto.response;

import cmc.mellyserver.mellycommon.enums.GroupType;
import cmc.mellyserver.mellycore.memory.domain.repository.dto.ImageDto;
import cmc.mellyserver.mellycore.memory.domain.repository.dto.MemoryResponseDto;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class MemoryResponse {

    private Long placeId;
    private String placeName;
    private Long memoryId;
    private List<ImageDto> memoryImages;
    private String title;
    private String content;
    private GroupType groupType;
    private String groupName;
    private Long stars;
    private List<String> keyword;
    private boolean loginUserWrite;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyyMMddHHmm")
    private LocalDateTime visitedDate;

    @Builder
    public MemoryResponse(Long placeId, String placeName, Long memoryId, List<ImageDto> imageDtos, String title,
                          String content, GroupType groupType, String groupName, Long stars, List<String> keyword, boolean loginUserWrite,
                          LocalDateTime visitedDate) {
        this.placeId = placeId;
        this.placeName = placeName;
        this.memoryId = memoryId;
        this.memoryImages = imageDtos;
        this.title = title;
        this.content = content;
        this.groupType = groupType;
        this.groupName = groupName;
        this.stars = stars;
        this.keyword = keyword;
        this.loginUserWrite = loginUserWrite;
        this.visitedDate = visitedDate;
    }

    public static MemoryResponse of(MemoryResponseDto memoryResponseDto) {
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
}
