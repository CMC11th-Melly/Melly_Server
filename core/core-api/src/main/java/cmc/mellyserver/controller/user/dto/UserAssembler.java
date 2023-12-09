package cmc.mellyserver.controller.user.dto;

import java.util.List;
import java.util.stream.Collectors;

import cmc.mellyserver.controller.user.dto.response.PlaceScrapCountResponse;
import cmc.mellyserver.domain.scrap.query.dto.PlaceScrapCountResponseDto;

public abstract class UserAssembler {

    public static List<PlaceScrapCountResponse> placeScrapCountResponses(
        List<PlaceScrapCountResponseDto> placeScrapCountResponseDtos) {
        return placeScrapCountResponseDtos.stream()
            .map(response -> PlaceScrapCountResponse.builder()
                .scrapType(response.getScrapType())
                .scrapCount(response.getScrapCount())
                .build())
            .collect(Collectors.toList());
    }

}
