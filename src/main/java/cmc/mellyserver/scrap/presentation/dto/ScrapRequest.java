package cmc.mellyserver.scrap.presentation.dto;

import cmc.mellyserver.common.enums.ScrapType;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ScrapRequest {

    @Schema(example = "34.0432423")
    private Double lat;
    @Schema(example = "127.0454544")
    private Double lng;
    @Schema(example = "COUPLE")
    private ScrapType scrapType;
    @Schema(example = "한양대학교")
    private String placeName;
    @Schema(example = "학교")
    private String placeCategory;
}
