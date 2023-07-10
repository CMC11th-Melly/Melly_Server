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

    /*
    readOnly 옵션 미적용 사유 : 설문 조사 조회 시나리오 상, 설문 조사를 생성 후 바로 추천 결과를 보여준다. 실제 추천 서비스가 적용될 시 시간 차가 존재하기에 레플리카로부터 데이터를 받아와도 되지만,
                            레플리카 렉을 고려하여 소스 DB로 부터 데이터를 가져온다.
     */
    @Transactional
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
