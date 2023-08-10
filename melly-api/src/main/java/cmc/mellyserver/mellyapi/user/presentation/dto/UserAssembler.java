package cmc.mellyserver.mellyapi.user.presentation.dto;

import cmc.mellyserver.mellyapi.user.presentation.dto.response.GroupLoginUserParticipatedResponse;
import cmc.mellyserver.mellyapi.user.presentation.dto.response.PlaceScrapCountResponse;
import cmc.mellyserver.mellyapi.user.presentation.dto.response.ScrapedPlaceResponse;
import cmc.mellyserver.mellycore.group.domain.repository.dto.GroupLoginUserParticipatedResponseDto;
import cmc.mellyserver.mellycore.scrap.domain.repository.dto.PlaceScrapCountResponseDto;
import cmc.mellyserver.mellycore.scrap.domain.repository.dto.ScrapedPlaceResponseDto;
import org.springframework.data.domain.Slice;

import java.util.List;
import java.util.stream.Collectors;

public abstract class UserAssembler {

    private UserAssembler() {
    }

    public static Slice<GroupLoginUserParticipatedResponse> groupLoginUserParticipatedResponses(
            Slice<GroupLoginUserParticipatedResponseDto> groupLoginUserParticipatedResponseDtos) {
        return groupLoginUserParticipatedResponseDtos.map(response ->
                GroupLoginUserParticipatedResponse.builder()
                        .groupId(response.getGroupId())
                        .groupType(response.getGroupType())
                        .groupName(response.getGroupName())
                        .groupIcon(response.getGroupIcon())
                        .build()
        );
    }

    public static Slice<ScrapedPlaceResponse> scrapedPlaceResponses(Slice<ScrapedPlaceResponseDto> scrapedPlaceResponseDtos) {
        return scrapedPlaceResponseDtos.map(response -> ScrapedPlaceResponse.builder()
                .placeId(response.getPlaceId())
                .placeName(response.getPlaceName())
                .placeImage(response.getPlaceImage())
                .placeCategory(response.getPlaceCategory())
                .isScraped(true)
                .build());
    }

    public static List<PlaceScrapCountResponse> placeScrapCountResponses(
            List<PlaceScrapCountResponseDto> placeScrapCountResponseDtos) {
        return placeScrapCountResponseDtos.stream().map(response ->
                        PlaceScrapCountResponse.builder()
                                .scrapType(response.getScrapType())
                                .scrapCount(response.getScrapCount()).build())
                .collect(Collectors.toList());
    }
}

// 요즘은 바로 프로필 수정 해버리네?
