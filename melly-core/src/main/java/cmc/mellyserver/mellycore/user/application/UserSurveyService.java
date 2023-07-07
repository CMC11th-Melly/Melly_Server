package cmc.mellyserver.mellycore.user.application;

import cmc.mellyserver.mellycore.common.AuthenticatedUserChecker;
import cmc.mellyserver.mellycore.user.application.dto.SurveyRecommendResponseDto;
import cmc.mellyserver.mellycore.user.application.dto.request.SurveyRequestDto;
import cmc.mellyserver.mellycore.user.application.survey.SurveyRecommender;
import cmc.mellyserver.mellycore.user.domain.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UserSurveyService {

    private final AuthenticatedUserChecker authenticatedUserChecker;
    private final SurveyRecommender surveyRecommender;

    @Transactional(readOnly = true)
    public SurveyRecommendResponseDto getSurveyResult(Long userSeq) {

        User user = authenticatedUserChecker.checkAuthenticatedUserExist(userSeq);
        return surveyRecommender.getRecommend(user.getRecommend().getRecommendGroup());
    }


    @Transactional
    public void createSurvey(SurveyRequestDto surveyRequestDto) {

        User user = authenticatedUserChecker.checkAuthenticatedUserExist(surveyRequestDto.getUserSeq());
        user.addSurveyData(surveyRequestDto.getRecommendGroup(), surveyRequestDto.getRecommendPlace(), surveyRequestDto.getRecommendActivity());
    }
}
