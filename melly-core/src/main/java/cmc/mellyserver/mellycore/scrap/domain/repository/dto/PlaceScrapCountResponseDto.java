package cmc.mellyserver.mellycore.scrap.domain.repository.dto;

import cmc.mellyserver.mellycore.common.enums.ScrapType;
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
