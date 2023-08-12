package cmc.mellyserver.mellycore.infrastructure.survey;

import cmc.mellyserver.mellycore.user.application.dto.response.SurveyRecommendResponseDto;
import cmc.mellyserver.mellycore.user.domain.enums.RecommendGroup;

public interface SurveyRecommender {
    SurveyRecommendResponseDto getRecommend(final RecommendGroup recommendGroup);

}
