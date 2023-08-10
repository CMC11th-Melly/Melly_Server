package cmc.mellyserver.mellycore.user.application.survey;

import cmc.mellyserver.mellycore.user.application.dto.response.SurveyRecommendResponseDto;
import cmc.mellyserver.mellycore.user.domain.enums.RecommendGroup;

public interface SurveyRecommender {
    SurveyRecommendResponseDto getRecommend(RecommendGroup recommendGroup);

}
