package cmc.mellyserver.user.presentation.dto;

import cmc.mellyserver.scrap.application.dto.response.PlaceScrapCountResponseDto;
import cmc.mellyserver.scrap.application.dto.response.ScrapedPlaceResponseDto;
import cmc.mellyserver.user.application.dto.response.GroupLoginUserParticipatedResponseDto;
import cmc.mellyserver.user.application.dto.response.ProfileUpdateFormResponseDto;
import cmc.mellyserver.user.application.dto.response.SurveyRecommendResponseDto;
import cmc.mellyserver.user.presentation.dto.response.*;
import org.springframework.data.domain.Slice;

import java.util.List;
import java.util.stream.Collectors;

public abstract class UserAssembler {

     public static List<GroupLoginUserParticipatedResponse> groupLoginUserParticipatedResponses(List<GroupLoginUserParticipatedResponseDto> groupLoginUserParticipatedResponseDtos)
     {
         return groupLoginUserParticipatedResponseDtos.stream().map(response ->
                 GroupLoginUserParticipatedResponse.builder()
                         .groupId(response.getGroupId())
                         .groupType(response.getGroupType())
                         .groupName(response.getGroupName())
                         .groupIcon(response.getGroupIcon())
                         .invitationLink(response.getInvitationLink())
                         .build()
         ).collect(Collectors.toList());
     }

     public static Slice<ScrapedPlaceResponse> scrapedPlaceResponses(Slice<ScrapedPlaceResponseDto> scrapedPlaceResponseDtos)
     {
         return scrapedPlaceResponseDtos.map(response -> ScrapedPlaceResponse.builder()
                 .placeId(response.getPlaceId())
                 .placeName(response.getPlaceName())
                 .placeImage(response.getPlaceImage())
                 .placeCategory(response.getPlaceCategory())
                 .myMemoryCount(response.getMyMemoryCount())
                 .otherMemoryCount(response.getOtherMemoryCount())
                 .position(response.getPosition())
                 .isScraped(response.getIsScraped())
                 .recommendType(response.getRecommendType())
                 .build());
     }

     public static List<PlaceScrapCountResponse> placeScrapCountResponses(List<PlaceScrapCountResponseDto> placeScrapCountResponseDtos)
     {
         return placeScrapCountResponseDtos.stream().map(response ->
                 PlaceScrapCountResponse.builder()
                         .scrapType(response.getScrapType())
                         .scrapCount(response.getScrapCount()).build())
                 .collect(Collectors.toList());
     }

    private UserAssembler(){}
}
