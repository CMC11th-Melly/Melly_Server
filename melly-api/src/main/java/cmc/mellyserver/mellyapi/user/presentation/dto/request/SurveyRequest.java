package cmc.mellyserver.mellyapi.user.presentation.dto.request;

import cmc.mellyserver.mellycommon.enums.RecommendActivity;
import cmc.mellyserver.mellycommon.enums.RecommendGroup;
import cmc.mellyserver.mellycommon.enums.RecommendPlace;
import cmc.mellyserver.mellycore.user.application.dto.request.SurveyRequestDto;
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

    public SurveyRequestDto toDto() {
        return new SurveyRequestDto(recommendGroup, recommendPlace, recommendActivity);
    }
}
