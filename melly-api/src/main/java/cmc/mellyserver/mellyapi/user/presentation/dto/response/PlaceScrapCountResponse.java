package cmc.mellyserver.mellyapi.user.presentation.dto.response;

import cmc.mellyserver.mellycommon.enums.ScrapType;
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
