package cmc.mellyserver.controller.memory.dto.response;

import java.time.LocalDate;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonFormat;

import cmc.mellyserver.dbcore.group.GroupType;
import cmc.mellyserver.domain.memory.query.dto.ImageDto;
import cmc.mellyserver.domain.memory.query.dto.KeywordDto;
import cmc.mellyserver.domain.memory.query.dto.MemoryDetailResponseDto;
import lombok.Builder;
import lombok.Data;

@Data
public class MemoryDetailResponse {

    // ==== place =====
    private Long placeId;

    private String placeName;

    // ==== memory ====

    private Long memoryId;

    private String title;

    private String content;

    private List<ImageDto> memoryImages;

    private List<KeywordDto> keyword;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private LocalDate visitedDate;

    private Long stars;

    // ==== group ====

    private Long groupId;

    private GroupType groupType;

    private String groupName;

    private int groupIcon;

    @Builder
    public MemoryDetailResponse(Long placeId, String placeName, Long memoryId, List<ImageDto> imageDtos, String title,
        String content, GroupType groupType, int groupIcon, String groupName, Long stars, List<KeywordDto> keyword,
        Long groupId, LocalDate visitedDate) {
        this.placeId = placeId;
        this.placeName = placeName;
        this.memoryId = memoryId;
        this.memoryImages = imageDtos;
        this.groupId = groupId;
        this.title = title;
        this.groupIcon = groupIcon;
        this.content = content;
        this.groupType = groupType;
        this.groupName = groupName;
        this.stars = stars;
        this.keyword = keyword;
        this.visitedDate = visitedDate;
    }

    public static MemoryDetailResponse of(MemoryDetailResponseDto memoryDetailResponseDto) {
        return MemoryDetailResponse.builder()
            .placeId(memoryDetailResponseDto.getPlaceId())
            .placeName(memoryDetailResponseDto.getPlaceName())
            .memoryId(memoryDetailResponseDto.getMemoryId())
            .imageDtos(memoryDetailResponseDto.getMemoryImages())
            .title(memoryDetailResponseDto.getTitle())
            .content(memoryDetailResponseDto.getContent())
            .groupId(memoryDetailResponseDto.getGroupId())
            .groupIcon(memoryDetailResponseDto.getGroupIcon())
            .groupType(memoryDetailResponseDto.getGroupType())
            .groupName(memoryDetailResponseDto.getGroupName())
            .stars(memoryDetailResponseDto.getStars())
            .keyword(memoryDetailResponseDto.getKeyword())
            .visitedDate(memoryDetailResponseDto.getVisitedDate())
            .build();
    }

}
