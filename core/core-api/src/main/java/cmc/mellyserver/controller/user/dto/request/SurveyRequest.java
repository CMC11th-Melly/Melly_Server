package cmc.mellyserver.controller.user.dto.request;

import cmc.mellyserver.dbcore.user.enums.RecommendActivity;
import cmc.mellyserver.dbcore.user.enums.RecommendGroup;
import cmc.mellyserver.dbcore.user.enums.RecommendPlace;
import cmc.mellyserver.domain.user.dto.request.SurveyRequestDto;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

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
