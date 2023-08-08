package cmc.mellyserver.mellycore.user.application.dto.request;

import cmc.mellyserver.mellycommon.enums.RecommendActivity;
import cmc.mellyserver.mellycommon.enums.RecommendGroup;
import cmc.mellyserver.mellycommon.enums.RecommendPlace;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class SurveyRequestDto {

    private RecommendGroup recommendGroup;

    private RecommendPlace recommendPlace;

    private RecommendActivity recommendActivity;

    @Builder
    public SurveyRequestDto(RecommendGroup recommendGroup, RecommendPlace recommendPlace, RecommendActivity recommendActivity) {
        this.recommendGroup = recommendGroup;
        this.recommendPlace = recommendPlace;
        this.recommendActivity = recommendActivity;
    }

}
