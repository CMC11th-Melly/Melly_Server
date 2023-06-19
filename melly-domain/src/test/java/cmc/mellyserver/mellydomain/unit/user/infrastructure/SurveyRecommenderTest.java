package cmc.mellyserver.mellydomain.unit.user.infrastructure;

import cmc.mellyserver.mellydomain.common.enums.RecommendGroup;
import cmc.mellyserver.mellydomain.user.infrastructure.SurveyRecommendResponseDto;
import cmc.mellyserver.mellydomain.user.infrastructure.SurveyRecommender;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

public class SurveyRecommenderTest {

    @DisplayName("함께하고 싶은 그룹 타입을 선택하면, 그에 맞는 장소를 추천해준다.")
    @ParameterizedTest
    @CsvSource({"COUPLE,연인과 성수동 맛집테이블", "FAMILY,가족과 잠실 산책길", "FRIEND,친구와 홍대 취미생활",
            "COMPANY,동료와 강남 카페 테이블"})
    void 설문조사_결과에_일치하는_장소를_추천해준다(RecommendGroup recommendGroup, String word) {

        // given
        SurveyRecommender surveyRecommender = new SurveyRecommender();

        // when
        SurveyRecommendResponseDto recommend = surveyRecommender.getRecommend(recommendGroup);

        // then
        Assertions.assertThat(recommend.getWords().get(0)).isEqualTo(word);
    }
}