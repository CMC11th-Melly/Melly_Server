package cmc.mellyserver.user.application;

import cmc.mellyserver.place.domain.Position;
import cmc.mellyserver.user.application.dto.PollRecommendResponse;
import cmc.mellyserver.user.domain.User;
import cmc.mellyserver.user.domain.enums.RecommendGroup;
import org.springframework.security.core.parameters.P;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.util.List;

@Component
public class SurveyRecommender {

    public PollRecommendResponse getRecommend(User user)
    {
        RecommendGroup recommendGroup = user.getRecommend().getRecommendGroup();
        PollRecommendResponse pollRecommendResponse;
        switch (recommendGroup)
        {
            case COUPLE:
                List<String> coupleWords = List.of("연인과 성수동 맛집테이블", "연인과 활동이 잦은 당신에게 추천해요", "맛집의 분위기 즐기고 추억도 쌓아보세요", "성수의 분위기 좋은 '성수다락' 추천해요");
                pollRecommendResponse =  new PollRecommendResponse(new Position(37.5000541000002,127.02425909999957),coupleWords);
                break;

            case FAMILY:
                List<String> familyWords = List.of("가족과 잠실 산책길", "가족과 활동이 잦은 당신에게 추천해요", "한적한 자연을 거닐며 추억을 쌓아보세요", "산책하기 좋은 '잠실 석촌호수'를 추천해요");
                pollRecommendResponse =  new PollRecommendResponse(new Position(37.5000541000002,127.02425909999957),familyWords);
                break;

            case FRIEND:
                List<String> friendWords = List.of("친구와 홍대 취미생활", "활동적으로 놀고 싶은 당신에게 추천해요", "도전과 운동을 즐기며 추억을 쌓아보세요", "암벽등반 장소 홍대 '더클라임'을 추천해요");
                pollRecommendResponse =   new PollRecommendResponse(new Position(37.5000541000002,127.02425909999957),friendWords);
                break;

            case COMPANY:
                List<String> companyWords = List.of("동료와 강남 카페 테이블", "동료와 시간을 보내는 당신에게 추천해요", "카페 분위기를 즐기며 추억을 쌓아보세요", "요즘 핫한 강남의 '빈터커피'를 추천해요");
                pollRecommendResponse =   new PollRecommendResponse(new Position(37.5000541000002,127.02425909999957),companyWords);
                break;

            default:
                pollRecommendResponse = null;
                break;

        }
        return pollRecommendResponse;
    }


}
