package cmc.mellyserver.user.presentation.dto.common;


import cmc.mellyserver.user.application.dto.PollRecommendResponse;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class SurveyRecommendResponseWrapper {

    private PollRecommendResponse surveyRecommend;
}