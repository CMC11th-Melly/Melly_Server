package cmc.mellyserver.scrap.application.dto.response;

import cmc.mellyserver.common.enums.ScrapType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PlaceScrapCountResponseDto {
    
    private ScrapType scrapType;
    private Long scrapCount;
}
