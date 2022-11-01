package cmc.mellyserver.user.presentation.dto.response;

import cmc.mellyserver.user.domain.enums.RecommendGroup;
import cmc.mellyserver.user.domain.enums.RecommendPlace;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class SurveyResponse {

    @Schema(description = "함께 하고 싶은 그룹",example = "FAMILY")
    private RecommendGroup recommendGroup;
    @Schema(description = "가고 싶은 장소",example = "성수")
    private RecommendPlace recommendPlace;
    @Schema(description = "추천 장소 보여줄때 필요한 장소 이름",example = "성수다락")
    private String placeName;
    @Schema(description = "위도",example = "37.5252525")
    private Double lat;
    @Schema(description = "경도",example = "127.35235236")
    private Double lng;
}
