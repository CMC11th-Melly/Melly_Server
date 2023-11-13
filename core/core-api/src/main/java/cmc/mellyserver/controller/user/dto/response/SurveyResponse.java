package cmc.mellyserver.controller.user.dto.response;

import cmc.mellyserver.dbcore.user.enums.RecommendGroup;
import cmc.mellyserver.dbcore.user.enums.RecommendPlace;
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
