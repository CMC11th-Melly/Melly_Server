package cmc.mellyserver.trend.application.dto;

import cmc.mellyserver.group.domain.enums.GroupType;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
@AllArgsConstructor
public class TrendResponseDto implements Serializable {
    private PlaceInfo placeInfo;
    private MemoryInfo memoryInfo;

    public TrendResponseDto(Long placeId,String placeImage, String placeCategory,GroupType recommendType,Boolean isScraped,String placeName,Long memoryId,List<String> memoryImages, String title, String content,String groupName,Integer stars)
    {
        this.placeInfo = new PlaceInfo(placeId,placeImage,placeCategory,recommendType,isScraped,placeName);
        this.memoryInfo = new MemoryInfo(memoryId,memoryImages, title,content,groupName,stars,List.of("최고에요","재밌어요"));
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
        private Integer stars;
        private List<String> keywords;

        public MemoryInfo(Long memoryId,List<String> memoryImages, String title, String content,String groupName,Integer stars, List<String> keywords)
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
