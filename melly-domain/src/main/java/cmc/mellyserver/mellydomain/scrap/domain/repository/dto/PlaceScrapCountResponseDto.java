package cmc.mellyserver.mellydomain.scrap.domain.repository.dto;

import cmc.mellyserver.mellydomain.common.enums.ScrapType;
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
