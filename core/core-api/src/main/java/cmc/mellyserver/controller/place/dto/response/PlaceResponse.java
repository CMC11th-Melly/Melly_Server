package cmc.mellyserver.controller.place.dto.response;


import cmc.mellyserver.dbcore.group.enums.GroupType;
import cmc.mellyserver.dbcore.place.Position;
import lombok.Builder;
import lombok.Data;

@Data
public class PlaceResponse {

    private Long placeId;
    private Position position;
    private Long myMemoryCount;
    private Long otherMemoryCount;
    private Boolean isScraped;
    private String PlaceCategory;
    private String placeName;
    private GroupType recommendType;
    private String placeImage;

    @Builder
    public PlaceResponse(Long placeId, Position position, Long myMemoryCount, Long otherMemoryCount, Boolean isScraped,
                         String placeCategory, String placeName, GroupType recommendType, String placeImage) {
        this.placeId = placeId;
        this.position = position;
        this.myMemoryCount = myMemoryCount;
        this.otherMemoryCount = otherMemoryCount;
        this.isScraped = isScraped;
        PlaceCategory = placeCategory;
        this.placeName = placeName;
        this.recommendType = recommendType;
        this.placeImage = placeImage;
    }
}