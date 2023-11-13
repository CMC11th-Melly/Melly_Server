package cmc.mellyserver.controller.user.dto.response;

import cmc.mellyserver.dbcore.scrap.ScrapType;
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
