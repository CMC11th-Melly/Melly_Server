package cmc.mellyserver.user.presentation.dto.response;

import cmc.mellyserver.common.enums.ScrapType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@Builder
public class PlaceScrapCountResponse {

    private ScrapType scrapType;
    private Long scrapCount;
}
