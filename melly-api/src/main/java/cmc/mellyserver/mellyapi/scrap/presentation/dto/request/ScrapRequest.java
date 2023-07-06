package cmc.mellyserver.mellyapi.scrap.presentation.dto.request;

import cmc.mellyserver.mellycommon.enums.ScrapType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ScrapRequest {

	private Double lat;

	private Double lng;

	private ScrapType scrapType;

	private String placeName;

	private String placeCategory;

}
