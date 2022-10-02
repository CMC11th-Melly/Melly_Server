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

    public TrendResponseDto(Long placeId,String placeImage, GroupType placeCategory,Boolean isScraped,String placeName,Long memoryId,String memoryImage, String title, String content,String groupName,Integer stars)
    {
        this.placeInfo = new PlaceInfo(placeId,placeImage,placeCategory,isScraped,placeName);
        this.memoryInfo = new MemoryInfo(memoryId,memoryImage, title,content,groupName,stars,List.of("최고에요","재밌어요"));
    }

    @Data
    static class PlaceInfo implements Serializable{
        private Long placeId;
        private String placeImage;
        private GroupType placeCategory;
        private Boolean isScraped;
        private String placeName;

        public PlaceInfo(Long placeId, String placeImage, GroupType placeCategory,Boolean isScraped,String placeName)
        {
            this.placeId = placeId;
            this.placeImage = placeImage;
            this.placeCategory = placeCategory;
            this.isScraped = isScraped;
            this.placeName = placeName;
        }
    }

    @Data
    static class MemoryInfo implements Serializable{
        private Long memoryId;
        private String memoryImage;
        private String title;
        private String content;
        private String groupName;
        private Integer stars;
        private List<String> keywords;

        public MemoryInfo(Long memoryId,String memoryImage, String title, String content,String groupName,Integer stars, List<String> keywords)
        {
            this.memoryId = memoryId;
            this.memoryImage = memoryImage;
            this.title = title;
            this.content = content;
            this.groupName = groupName;
            this.stars =stars;
            this.keywords = keywords;
        }
    }



}
