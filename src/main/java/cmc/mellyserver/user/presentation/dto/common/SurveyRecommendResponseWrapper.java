package cmc.mellyserver.user.presentation.dto.common;


import cmc.mellyserver.user.application.dto.SurveyRecommendResponse;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class SurveyRecommendResponseWrapper {

    private SurveyRecommendResponse surveyRecommend;
}