package cmc.mellyserver.scrap.application.dto.response;

import cmc.mellyserver.common.enums.ScrapType;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class PlaceScrapCountResponseDto {
    
    private ScrapType scrapType;
    private Long scrapCount;
}
