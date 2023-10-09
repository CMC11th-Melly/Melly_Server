package cmc.mellyserver.domain.scrap.dto;


import cmc.mellyserver.dbcore.place.Place;
import cmc.mellyserver.dbcore.place.Position;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.util.HashMap;

@Data
@AllArgsConstructor
public class PlaceResponseDto {

    private Long placeId = -1L;
    private Position position;
    private Long myMemoryCount;
    private Long otherMemoryCount;
    private Boolean isScraped;
    private String PlaceCategory = "";
    private String placeName = "";
    private String placeImage;

    @Builder
    private PlaceResponseDto(Position position, Long myMemoryCount, Long otherMemoryCount, Boolean isScraped) {

        this.position = position;
        this.myMemoryCount = myMemoryCount;
        this.otherMemoryCount = otherMemoryCount;
        this.isScraped = isScraped;
        this.placeImage = "https://mellyimage.s3.ap-northeast-2.amazonaws.com/KakaoTalk_20221118_193556196.png";
    }

    public static PlaceResponseDto of(Place place, boolean isUserScraped, HashMap<String, Long> memoryCount) {
        return new PlaceResponseDto(place.getId(), place.getPosition(),
                memoryCount.get("myMemory"),
                memoryCount.get("otherMemory"),
                isUserScraped, place.getPlaceCategory(), place.getPlaceName(),
                place.getPlaceImage());
    }

}
