package cmc.mellyserver.trend.application.dto;

import cmc.mellyserver.group.domain.enums.GroupType;
import cmc.mellyserver.memory.domain.Memory;
import cmc.mellyserver.recommend.application.dto.RecommendResponseDto;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Data
@AllArgsConstructor
public class TrendResponseDto implements Serializable {
    private PlaceInfo placeInfo;
    private List<MemoryInfo> memoryInfo;

    public TrendResponseDto(Long placeId, String placeImage, String placeCategory, GroupType recommendType, Boolean isScraped, String placeName, List<Memory> memories, String groupName)
    {
        this.placeInfo = new PlaceInfo(placeId,placeImage,placeCategory,recommendType,isScraped,placeName);
        this.memoryInfo = memories.stream().map(m ->
                new MemoryInfo(m.getId(),
                        m.getMemoryImages().stream().
                                map(tm -> tm.getImagePath()).collect(Collectors.toList()),
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
            this.recommendType= recommendType;
            this.isScraped = isScraped;
            this.placeName = placeName;
        }
    }

    @Data
    static class MemoryInfo implements Serializable{
        private Long memoryId;
        private List<String> memoryImages;
        private String title;
        private String content;
        private GroupType groupType;
        private String groupName;
        private Long stars;
        private String keyword;
        @JsonFormat(shape= JsonFormat.Shape.STRING, pattern="yyyyMMdd")
        private LocalDateTime visitedDate;

        public MemoryInfo(Long memoryId,List<String> memoryImages, String title, String content,GroupType groupType, String groupName,Long stars, String keyword,LocalDateTime visitedDate)
        {
            this.memoryId = memoryId;
            this.memoryImages = memoryImages;
            this.title = title;
            this.content = content;
            this.groupType = groupType;
            this.groupName = groupName;
            this.stars =stars;
            this.keyword = keyword;
            this.visitedDate = visitedDate;
        }
    }



}
