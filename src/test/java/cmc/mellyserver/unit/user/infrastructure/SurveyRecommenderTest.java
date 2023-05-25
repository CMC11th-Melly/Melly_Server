package cmc.mellyserver.unit.user.infrastructure;

import cmc.mellyserver.common.enums.RecommendGroup;
import cmc.mellyserver.common.factory.UserFactory;
import cmc.mellyserver.place.domain.Position;
import cmc.mellyserver.user.application.dto.SurveyRecommendResponseDto;
import cmc.mellyserver.user.domain.User;
import cmc.mellyserver.user.infrastructure.SurveyRecommender;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import java.util.List;

public class SurveyRecommenderTest {

    @DisplayName("유저의 설문 조사 결과가 주어졌을때")
    @Nested
    class When_SurveyResult_Given{

        @DisplayName("설문 조사 결과를 분석해서 알려준다.")
        @Test
        void test(){

            // given
            User emailLoginUser = UserFactory.createEmailLoginUser();
            emailLoginUser.addSurveyData(RecommendGroup.FRIEND,"홍대","액티비티");
            SurveyRecommender surveyRecommender = new SurveyRecommender();

            // when
            SurveyRecommendResponseDto recommend = surveyRecommender.getRecommend(emailLoginUser);

            // then
            List<String> words = recommend.getWords();
            Assertions.assertThat(words).hasSize(4);
            Assertions.assertThat(words.get(0)).isEqualTo("친구와 홍대 취미생활");
            Assertions.assertThat(recommend.getPosition()).isEqualTo(new Position(37.538969, 127.139775));
        }
    }
}
