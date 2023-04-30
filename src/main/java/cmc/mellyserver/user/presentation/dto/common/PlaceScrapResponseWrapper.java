package cmc.mellyserver.user.presentation.dto.common;

import cmc.mellyserver.scrap.application.dto.PlaceScrapResponseDto;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class PlaceScrapResponseWrapper {
    private List<PlaceScrapResponseDto> scrapCount;
}
