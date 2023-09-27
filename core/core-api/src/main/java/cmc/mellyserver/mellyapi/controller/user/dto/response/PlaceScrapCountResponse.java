package cmc.mellyserver.mellyapi.controller.user.dto.response;

import cmc.mellyserver.dbcore.scrap.enums.ScrapType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@AllArgsConstructor
@Builder
public class PlaceScrapCountResponse {

    private ScrapType scrapType;
    private Long scrapCount;
}
