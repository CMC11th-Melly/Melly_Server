package cmc.mellyserver.user.presentation.dto.request;

import cmc.mellyserver.common.enums.RecommendGroup;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class SurveyRequestDto {

    private Long userSeq;

    private RecommendGroup recommendGroup;
    private String recommendPlace;
    private String recommendActivity;

    @Builder
    public SurveyRequestDto(Long userSeq, RecommendGroup recommendGroup, String recommendPlace, String recommendActivity) {
        this.userSeq = userSeq;
        this.recommendGroup = recommendGroup;
        this.recommendPlace = recommendPlace;
        this.recommendActivity = recommendActivity;
    }

    public static SurveyRequestDto of(Long userSeq, SurveyRequest surveyRequest)
    {
        return SurveyRequestDto.builder()
                .userSeq(userSeq)
                .recommendGroup(surveyRequest.getRecommendGroup())
                .recommendPlace(surveyRequest.getRecommendPlace())
                .recommendActivity(surveyRequest.getRecommendActivity())
                .build();
    }
}
