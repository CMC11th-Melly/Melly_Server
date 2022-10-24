package cmc.mellyserver.recommend.application.dto;

import cmc.mellyserver.group.domain.enums.GroupType;
import cmc.mellyserver.memory.domain.Memory;
import cmc.mellyserver.memory.presentation.dto.ImageDto;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;

import javax.swing.*;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Data
@AllArgsConstructor
public class RecommendResponseDto implements Serializable {
    private PlaceInfo placeInfo;
    private List<MemoryInfo> memoryInfo;

    public RecommendResponseDto(Long placeId, String placeImage, String placeCategory, GroupType recommendType, Boolean isScraped, String placeName, List<Memory> memories,String groupName)
    {
        this.placeInfo = new PlaceInfo(placeId,placeImage,placeCategory,recommendType,isScraped,placeName);
        this.memoryInfo = memories.stream().map(m ->
                new MemoryInfo(m.getId(),
                        m.getMemoryImages().stream().
                                map(tm -> new ImageDto(tm.getId(),tm.getImagePath())).collect(Collectors.toList()),
                        m.getTitle(),
                        m.getContent(),
                        m.getGroupInfo().getGroupType(),
                        m.getGroupInfo().getGroupName(),
                        m.getStars(),
                        m.getKeyword(),
                        m.getVisitedDate()))
                .collect(Collectors.toList());

    }

    @Data
    static class PlaceInfo implements Serializable{
        private Long placeId;
        private String placeImage;
        private String placeCategory;
        private GroupType recommendType;
        private Boolean isScraped;
        private String placeName;

        public PlaceInfo(Long placeId, String placeImage, String placeCategory,GroupType recommendType,Boolean isScraped,String placeName)
        {
            this.placeId = placeId;
            this.placeImage = placeImage;
            this.placeCategory = placeCategory;
            this.recommendType = recommendType;
            this.isScraped = isScraped;
            this.placeName = placeName;
        }
    }

    @Data
    static class MemoryInfo implements Serializable{
        @Schema(example = "1")
        private Long memoryId;
        @Schema(example = "[melly.jpg,cmc.png]")
        private List<ImageDto> memoryImages;
        @Schema(example = "오랜만에 고향 친구랑!")
        private String title;
        @Schema(example = "다음에 친구들 데리고 다시 와야지!")
        private String content;
        @Schema(example = "FRIEND")
        private GroupType groupType;
        @Schema(example = "떡잎마을방범대")
        private String groupName;
        @Schema(example = "4.5")
        private Long stars;
        @Schema(example = "최고에요")
        private List<String> keyword;
        @Schema(example = "20221014")
        @JsonFormat(shape= JsonFormat.Shape.STRING, pattern="yyyyMMdd")
        private LocalDateTime visitedDate;

        public MemoryInfo(Long memoryId,List<ImageDto> memoryImages, String title, String content,GroupType groupType,String groupName,Long stars, List<String> keyword,LocalDateTime visitiedDate)
        {
            this.memoryId = memoryId;
            this.memoryImages = memoryImages;
            this.title = title;
            this.content = content;
            this.groupType = groupType;
            this.groupName = groupName;
            this.stars =stars;
            this.keyword = keyword;
            this.visitedDate = visitiedDate;
        }
    }



}
