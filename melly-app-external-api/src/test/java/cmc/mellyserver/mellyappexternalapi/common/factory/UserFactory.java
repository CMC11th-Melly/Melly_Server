package cmc.mellyserver.mellyappexternalapi.common.factory;

import cmc.mellyserver.mellyappexternalapi.user.application.dto.request.SurveyRequestDto;
import cmc.mellyserver.mellyappexternalapi.user.presentation.dto.request.SurveyRequest;
import cmc.mellyserver.mellydomain.common.enums.*;
import cmc.mellyserver.mellydomain.place.domain.Position;
import cmc.mellyserver.mellydomain.user.domain.User;
import cmc.mellyserver.mellydomain.user.infrastructure.SurveyRecommendResponseDto;

import java.util.List;
import java.util.UUID;

public class UserFactory {

    public static User createEmailLoginUser() {
        return User.builder()
                .uid(UUID.randomUUID().toString())
                .email("melly@gmail.com")
                .password("asdfasdf")
                .roleType(RoleType.USER)
                .gender(Gender.MALE)
                .provider(Provider.DEFAULT)
                .ageGroup(AgeGroup.TWO)
                .profileImage("cmc11th.jpg")
                .nickname("떡잎마을방범대")
                .isDeleted(DeleteStatus.N)
                .build();
    }

    public static SurveyRequestDto mockSurveyRequestDto() {
        return SurveyRequestDto.builder()
                .userSeq(1L)
                .recommendGroup(RecommendGroup.FRIEND)
                .recommendPlace(RecommendPlace.PLACE1)
                .recommendActivity(RecommendActivity.CAFE)
                .build();
    }

    public static SurveyRequest mockSurveyRequest() {
        return SurveyRequest.builder()
                .recommendGroup(RecommendGroup.FRIEND)
                .recommendPlace(RecommendPlace.PLACE1)
                .recommendActivity(RecommendActivity.CAFE)
                .build();
    }

    public static SurveyRecommendResponseDto mockSurveyRecommendResponseDto() {
        return SurveyRecommendResponseDto.builder()
                .position(new Position(1.234, 1.234))
                .words(List.of("재밌어요", "기뻐요"))
                .build();
    }

}
