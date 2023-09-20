package cmc.mellyserver.mellycore.user.application;


import cmc.mellyserver.mellycore.infrastructure.survey.SurveyRecommender;
import cmc.mellyserver.mellycore.user.application.dto.request.SurveyRequestDto;
import cmc.mellyserver.mellycore.user.application.dto.response.SurveyRecommendResponseDto;
import cmc.mellyserver.mellycore.user.domain.User;
import cmc.mellyserver.mellycore.user.domain.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UserSurveyService {

    private final UserRepository userRepository;
    private final SurveyRecommender surveyRecommender;


    @Transactional
    public SurveyRecommendResponseDto getSurveyResult(final Long userId) {

        User user = userRepository.getById(userId);
        return surveyRecommender.getRecommend(user.getRecommend().getRecommendGroup());
    }

    @Transactional
    public void createSurvey(final Long userId, SurveyRequestDto surveyRequestDto) {

        User user = userRepository.getById(userId);
        user.addSurveyData(surveyRequestDto.getRecommendGroup(), surveyRequestDto.getRecommendPlace(), surveyRequestDto.getRecommendActivity());
    }
}
