package cmc.mellyserver.scrap.presentation.dto.request;

import cmc.mellyserver.common.enums.ScrapType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ScrapRequest {

    private Double lat;
    private Double lng;
    private ScrapType scrapType;
    private String placeName;
    private String placeCategory;
}
