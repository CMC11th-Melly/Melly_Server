package cmc.mellyserver.mellycore.user.application.survey;

import cmc.mellyserver.mellycommon.enums.RecommendGroup;
import cmc.mellyserver.mellycore.user.application.dto.response.SurveyRecommendResponseDto;

public interface SurveyRecommender {
    SurveyRecommendResponseDto getRecommend(RecommendGroup recommendGroup);

}
