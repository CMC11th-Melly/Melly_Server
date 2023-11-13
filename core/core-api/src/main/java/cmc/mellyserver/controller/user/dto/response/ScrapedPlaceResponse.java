package cmc.mellyserver.controller.user.dto.response;

import lombok.Builder;
import lombok.Data;

@Data
public class ScrapedPlaceResponse {

    private Long placeId;

    private String placeCategory;

    private String placeName;

    private String placeImage;

    private Boolean isScraped;

    @Builder
    public ScrapedPlaceResponse(Long placeId, String placeCategory, String placeName, String placeImage,
        Boolean isScraped) {

        this.placeId = placeId;
        this.placeCategory = placeCategory;
        this.placeName = placeName;
        this.placeImage = placeImage;
        this.isScraped = isScraped;
    }

}
