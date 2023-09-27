package cmc.mellyserver.mellyapi.controller.user.dto;

import cmc.mellyserver.mellyapi.controller.user.dto.response.PlaceScrapCountResponse;
import cmc.mellyserver.mellyapi.domain.scrap.query.dto.PlaceScrapCountResponseDto;

import java.util.List;
import java.util.stream.Collectors;

public abstract class UserAssembler {

    private UserAssembler() {
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
