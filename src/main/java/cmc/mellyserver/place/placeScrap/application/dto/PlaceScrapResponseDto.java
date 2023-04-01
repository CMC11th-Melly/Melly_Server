package cmc.mellyserver.place.placeScrap.application.dto;

import cmc.mellyserver.common.enums.ScrapType;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class PlaceScrapResponseDto {
    
    private ScrapType scrapType;
    private Long scrapCount;
}
