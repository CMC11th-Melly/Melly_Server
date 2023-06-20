package cmc.mellyserver.mellyappexternalapi.user.presentation.dto.response;

import cmc.mellyserver.mellydomain.common.enums.RecommendGroup;
import cmc.mellyserver.mellydomain.common.enums.RecommendPlace;
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
