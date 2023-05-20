package cmc.mellyserver.user.presentation.dto;

import cmc.mellyserver.user.application.dto.ProfileUpdateFormResponseDto;
import cmc.mellyserver.user.application.dto.SurveyRecommendResponseDto;
import cmc.mellyserver.user.presentation.dto.response.ProfileUpdateFormResponse;
import cmc.mellyserver.user.presentation.dto.response.SurveyRecommendResponse;

public class UserAssembler {

     public static SurveyRecommendResponse surveyRecommendResponse(SurveyRecommendResponseDto surveyRecommendResponseDto)
     {
         return SurveyRecommendResponse.builder()
                 .position(surveyRecommendResponseDto.getPosition())
                 .words(surveyRecommendResponseDto.getWords())
                 .build();
     }


     public static ProfileUpdateFormResponse profileUpdateFormResponse(ProfileUpdateFormResponseDto profileUpdateFormResponseDto)
     {
         return ProfileUpdateFormResponse.builder()
                 .nickname(profileUpdateFormResponseDto.getNickname())
                 .profileImage(profileUpdateFormResponseDto.getProfileImage())
                 .gender(profileUpdateFormResponseDto.getGender())
                 .ageGroup(profileUpdateFormResponseDto.getAgeGroup())
                 .build();
     }
}
