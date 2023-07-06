package cmc.mellyserver.mellyapi.common.factory;

import cmc.mellyserver.mellyapi.user.presentation.dto.request.SurveyRequest;
import cmc.mellyserver.mellycommon.enums.*;
import cmc.mellyserver.mellycore.common.enums.*;
import cmc.mellyserver.mellycore.place.domain.Position;
import cmc.mellyserver.mellycore.user.application.dto.SurveyRecommendResponseDto;
import cmc.mellyserver.mellycore.user.application.dto.request.SurveyRequestDto;
import cmc.mellyserver.mellycore.user.domain.User;

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
