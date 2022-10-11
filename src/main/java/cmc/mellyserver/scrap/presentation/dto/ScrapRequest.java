package cmc.mellyserver.scrap.presentation.dto;

import cmc.mellyserver.group.domain.enums.GroupType;
import cmc.mellyserver.place.domain.enums.ScrapType;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
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
