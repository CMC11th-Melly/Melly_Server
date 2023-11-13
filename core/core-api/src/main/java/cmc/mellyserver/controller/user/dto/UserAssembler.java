package cmc.mellyserver.controller.user.dto;

import java.util.List;
import java.util.stream.Collectors;

import cmc.mellyserver.controller.user.dto.response.PlaceScrapCountResponse;
import cmc.mellyserver.domain.scrap.query.dto.PlaceScrapCountResponseDto;

/**
 * Assembler는 DTO List의 타입 변환 같이 코드가 복잡해지는 작업을 캡슐화 하기 위한 유틸 클래스입니다.
 */
public abstract class UserAssembler {

    private UserAssembler() {
    }

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
