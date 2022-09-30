package cmc.mellyserver.recommend.presentation.dto;

import cmc.mellyserver.group.domain.GroupType;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class TrendPlaceResponse {
    private PlaceInfo placeInfo;
    private MemoryInfo memoryInfo;


    static class PlaceInfo{
        private String placeImage;
        private GroupType groupType;
        private String placeCategory;
        private Boolean isScraped;
        private String placeName;

        public PlaceInfo(String placeImage, GroupType groupType,String placeCategory,Boolean isScraped,String placeName)
        {
            this.placeImage = placeImage;
            this.groupType =groupType;
            this.placeCategory = placeCategory;
            this.isScraped = isScraped;
            this.placeName = placeName;
        }
    }

    static class MemoryInfo{
         private String memoryImage;
         private String title;
         private String content;

         public MemoryInfo(String memoryImage, String title, String content)
         {
             this.memoryImage = memoryImage;
             this.title = title;
             this.content = content;
         }
    }
}
