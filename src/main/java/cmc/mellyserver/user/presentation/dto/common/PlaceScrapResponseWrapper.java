package cmc.mellyserver.user.presentation.dto.common;

import cmc.mellyserver.scrap.application.dto.response.PlaceScrapCountResponseDto;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class PlaceScrapResponseWrapper {
    private List<PlaceScrapCountResponseDto> scrapCount;
}
