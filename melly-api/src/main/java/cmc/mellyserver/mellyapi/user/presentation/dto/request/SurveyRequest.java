package cmc.mellyserver.mellyapi.user.presentation.dto.request;

import cmc.mellyserver.mellycore.user.application.dto.request.SurveyRequestDto;
import cmc.mellyserver.mellycore.user.domain.enums.RecommendActivity;
import cmc.mellyserver.mellycore.user.domain.enums.RecommendGroup;
import cmc.mellyserver.mellycore.user.domain.enums.RecommendPlace;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;

@Data
@NoArgsConstructor
public class SurveyRequest {

    @NotNull(message = "추천 그룹을 입력해주세요.")
    private RecommendGroup recommendGroup;

    @NotNull(message = "추천 그룹을 입력해주세요.")
    private RecommendPlace recommendPlace;

    @NotNull(message = "추천 그룹을 입력해주세요.")
    private RecommendActivity recommendActivity;

    @Builder
    public SurveyRequest(RecommendGroup recommendGroup, RecommendPlace recommendPlace,
                         RecommendActivity recommendActivity) {
        this.recommendGroup = recommendGroup;
        this.recommendPlace = recommendPlace;
        this.recommendActivity = recommendActivity;
    }

    public SurveyRequestDto toServiceRequest() {
        return new SurveyRequestDto(recommendGroup, recommendPlace, recommendActivity);
    }
}
