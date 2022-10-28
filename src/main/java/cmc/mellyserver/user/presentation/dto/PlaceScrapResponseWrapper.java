package cmc.mellyserver.user.presentation.dto;

import cmc.mellyserver.placeScrap.application.dto.PlaceScrapResponseDto;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class PlaceScrapResponseWrapper {
    private List<PlaceScrapResponseDto> scrapCounts;
}
