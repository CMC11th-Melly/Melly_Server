package cmc.mellyserver.domain.scrap.query.dto;

import java.io.Serializable;

import cmc.mellyserver.dbcore.scrap.ScrapType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PlaceScrapCountResponseDto implements Serializable {

	private ScrapType scrapType;

	private Long scrapCount;

}
