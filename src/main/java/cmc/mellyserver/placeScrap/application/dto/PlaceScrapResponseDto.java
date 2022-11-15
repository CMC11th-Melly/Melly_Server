package cmc.mellyserver.placeScrap.application.dto;

import cmc.mellyserver.place.domain.enums.ScrapType;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class PlaceScrapResponseDto {
    
    private ScrapType scrapType;
    private Long scrapCount;
}
