package cmc.mellyserver.user.presentation.dto.response;

import cmc.mellyserver.common.enums.RecommendGroup;
import cmc.mellyserver.common.enums.RecommendPlace;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class SurveyResponse {

    private RecommendGroup recommendGroup;
    private RecommendPlace recommendPlace;
    private String placeName;
    private Double lat;
    private Double lng;
}
