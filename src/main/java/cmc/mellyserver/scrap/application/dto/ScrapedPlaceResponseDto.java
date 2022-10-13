package cmc.mellyserver.scrap.application.dto;

import cmc.mellyserver.group.domain.enums.GroupType;
import cmc.mellyserver.place.domain.Position;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ScrapedPlaceResponseDto {
    private Long placeId;
    private Position position;
    private Long myMemoryCount;
    private Long otherMemoryCount;
    private Boolean isScraped;
    private String placeName;
    private GroupType recommendType;
    private String placeImage;
    private String placeCategory;


}
