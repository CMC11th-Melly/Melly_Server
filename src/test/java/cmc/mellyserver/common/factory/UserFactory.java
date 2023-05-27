package cmc.mellyserver.common.factory;

import cmc.mellyserver.common.enums.*;
import cmc.mellyserver.user.application.dto.response.ProfileUpdateFormResponseDto;
import cmc.mellyserver.user.domain.User;
import cmc.mellyserver.user.presentation.dto.request.ProfileUpdateRequest;
import cmc.mellyserver.user.presentation.dto.request.SurveyRequest;
import cmc.mellyserver.user.presentation.dto.request.SurveyRequestDto;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.UUID;

public class UserFactory {

    public static User createEmailLoginUser()
    {
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
                .build();
    }

    public static SurveyRequestDto mockSurveyRequestDto()
    {
        return SurveyRequestDto.builder()
                .userSeq(1L)
                .recommendGroup(RecommendGroup.FRIEND)
                .recommendPlace(RecommendPlace.PLACE1)
                .recommendActivity(RecommendActivity.CAFE)
                .build();
    }

    public static SurveyRequest mockSurveyRequest()
    {
        return SurveyRequest.builder()
                .recommendGroup(RecommendGroup.FRIEND)
                .recommendPlace(RecommendPlace.PLACE1)
                .recommendActivity(RecommendActivity.CAFE)
                .build();
    }

    public static ProfileUpdateRequest mockProfileUpdateRequest() throws IOException {
        return ProfileUpdateRequest.builder()
                .deleteImage(false)
                .nickname("test nickname")
                .gender(Gender.MALE)
                .ageGroup(AgeGroup.ONE)
                .profileImage(new MockMultipartFile("content","filename","multipart/form-data", new FileInputStream("/Users/seojemin/IdeaProjects/Melly_Server/src/test/resources/image/testimage.jpg")))
                .build();
    }
}
