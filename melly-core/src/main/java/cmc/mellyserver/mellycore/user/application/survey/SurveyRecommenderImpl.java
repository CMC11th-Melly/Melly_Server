package cmc.mellyserver.mellycore.user.application.survey;

import cmc.mellyserver.mellycommon.enums.RecommendGroup;
import cmc.mellyserver.mellycore.place.domain.Position;
import cmc.mellyserver.mellycore.user.application.dto.response.SurveyRecommendResponseDto;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class SurveyRecommenderImpl implements SurveyRecommender {

    public SurveyRecommendResponseDto getRecommend(RecommendGroup recommendGroup) {

        SurveyRecommendResponseDto pollRecommendResponse;

        switch (recommendGroup) {
            case COUPLE:
                List<String> coupleWords = List.of("연인과 성수동 맛집테이블", "연인과 활동이 잦은 당신에게 추천해요",
                        "맛집의 분위기 즐기고 추억도 쌓아보세요",
                        "성수의 분위기 좋은 '성수다락' 추천해요");
                pollRecommendResponse = new SurveyRecommendResponseDto(
                        new Position(37.5470041, 127.0426674),
                        coupleWords);
                break;

            case FAMILY:
                List<String> familyWords = List.of("가족과 잠실 산책길", "가족과 활동이 잦은 당신에게 추천해요",
                        "한적한 자연을 거닐며 추억을 쌓아보세요",
                        "산책하기 좋은 '잠실 석촌호수'를 추천해요");
                pollRecommendResponse = new SurveyRecommendResponseDto(
                        new Position(37.5113096, 127.1051525),
                        familyWords);
                break;

            case FRIEND:
                List<String> friendWords = List.of("친구와 홍대 취미생활", "활동적으로 놀고 싶은 당신에게 추천해요",
                        "도전과 운동을 즐기며 추억을 쌓아보세요",
                        "암벽등반 장소 홍대 '더클라임'을 추천해요");
                pollRecommendResponse = new SurveyRecommendResponseDto(
                        new Position(37.538969, 127.139775),
                        friendWords);
                break;

            case COMPANY:
                List<String> companyWords = List.of("동료와 강남 카페 테이블", "동료와 시간을 보내는 당신에게 추천해요",
                        "카페 분위기를 즐기며 추억을 쌓아보세요",
                        "요즘 핫한 강남의 '빈터커피'를 추천해요");
                pollRecommendResponse = new SurveyRecommendResponseDto(
                        new Position(37.5389526, 127.1397714),
                        companyWords);
                break;

            default:
                pollRecommendResponse = null;
                break;

        }
        return pollRecommendResponse;
    }

}
