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

    private Long id;

    private RecommendGroup recommendGroup;

    private RecommendPlace recommendPlace;

    private RecommendActivity recommendActivity;

    @Builder
    public SurveyRequestDto(Long id, RecommendGroup recommendGroup, RecommendPlace recommendPlace,
                            RecommendActivity recommendActivity) {
        this.id = id;
        this.recommendGroup = recommendGroup;
        this.recommendPlace = recommendPlace;
        this.recommendActivity = recommendActivity;
    }

}
