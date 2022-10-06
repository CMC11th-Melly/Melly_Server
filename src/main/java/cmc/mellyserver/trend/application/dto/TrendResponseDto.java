package cmc.mellyserver.trend.application.dto;

import cmc.mellyserver.group.domain.enums.GroupType;
import cmc.mellyserver.memory.domain.Memory;
import cmc.mellyserver.recommend.application.dto.RecommendResponseDto;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.io.Serializable;
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
                        groupName,
                        m.getStars(),
                        m.getKeyword()))
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
        private String groupName;
        private Long stars;
        private List<String> keywords;

        public MemoryInfo(Long memoryId,List<String> memoryImages, String title, String content,String groupName,Long stars, List<String> keywords)
        {
            this.memoryId = memoryId;
            this.memoryImages = memoryImages;
            this.title = title;
            this.content = content;
            this.groupName = groupName;
            this.stars =stars;
            this.keywords = keywords;
        }
    }



}
