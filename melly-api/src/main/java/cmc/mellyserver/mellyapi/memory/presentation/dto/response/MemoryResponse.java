package cmc.mellyserver.mellyapi.memory.presentation.dto.response;

import cmc.mellyserver.mellycore.group.domain.enums.GroupType;
import cmc.mellyserver.mellycore.memory.domain.repository.dto.ImageDto;
import cmc.mellyserver.mellycore.memory.domain.repository.dto.KeywordDto;
import cmc.mellyserver.mellycore.memory.domain.repository.dto.MemoryResponseDto;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;
import java.util.List;

@Data
public class MemoryResponse {

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
    public MemoryResponse(Long placeId, String placeName, Long memoryId, List<ImageDto> imageDtos, String title,
                          String content, GroupType groupType, String groupName, Long stars, List<KeywordDto> keyword,
                          LocalDate visitedDate) {
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
        this.visitedDate = visitedDate;
    }

    public static MemoryResponse of(MemoryResponseDto memoryResponseDto) {
        return MemoryResponse.builder()
                .memoryId(memoryResponseDto.getMemoryId())
                .title(memoryResponseDto.getTitle())
                .groupType(memoryResponseDto.getGroupType())
                .visitedDate(memoryResponseDto.getVisitedDate())
                .build();
    }
}
