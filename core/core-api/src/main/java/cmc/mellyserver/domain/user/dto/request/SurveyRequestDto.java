package cmc.mellyserver.domain.user.dto.request;

import cmc.mellyserver.dbcore.user.enums.RecommendActivity;
import cmc.mellyserver.dbcore.user.enums.RecommendGroup;
import cmc.mellyserver.dbcore.user.enums.RecommendPlace;
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
    public SurveyRequestDto(RecommendGroup recommendGroup, RecommendPlace recommendPlace,
        RecommendActivity recommendActivity) {
        this.recommendGroup = recommendGroup;
        this.recommendPlace = recommendPlace;
        this.recommendActivity = recommendActivity;
    }

}
