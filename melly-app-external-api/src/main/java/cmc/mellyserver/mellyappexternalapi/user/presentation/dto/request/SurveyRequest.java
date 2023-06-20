package cmc.mellyserver.mellyappexternalapi.user.presentation.dto.request;

import cmc.mellyserver.mellydomain.common.enums.RecommendActivity;
import cmc.mellyserver.mellydomain.common.enums.RecommendGroup;
import cmc.mellyserver.mellydomain.common.enums.RecommendPlace;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class SurveyRequest {

    private RecommendGroup recommendGroup;
    private RecommendPlace recommendPlace;
    private RecommendActivity recommendActivity;

    @Builder
    public SurveyRequest(RecommendGroup recommendGroup, RecommendPlace recommendPlace,
            RecommendActivity recommendActivity) {
        this.recommendGroup = recommendGroup;
        this.recommendPlace = recommendPlace;
        this.recommendActivity = recommendActivity;
    }
}
