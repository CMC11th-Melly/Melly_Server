package cmc.mellyserver.recommend.application.dto;

import cmc.mellyserver.group.domain.enums.GroupType;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.io.Serializable;

@Data
@AllArgsConstructor
public class RecommendResponseDto implements Serializable {
    private PlaceInfo placeInfo;
    private MemoryInfo memoryInfo;

    public RecommendResponseDto(Long placeId,String placeImage, GroupType placeCategory,Boolean isScraped,String placeName,Long memoryId,String memoryImage, String title, String content)
    {
        this.placeInfo = new PlaceInfo(placeId,placeImage,placeCategory,isScraped,placeName);
        this.memoryInfo = new MemoryInfo(memoryId,memoryImage, title,content);
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

        public MemoryInfo(Long memoryId,String memoryImage, String title, String content)
        {
            this.memoryId = memoryId;
            this.memoryImage = memoryImage;
            this.title = title;
            this.content = content;
        }
    }



}
