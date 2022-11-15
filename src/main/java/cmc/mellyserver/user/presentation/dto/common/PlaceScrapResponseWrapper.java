package cmc.mellyserver.user.presentation.dto.common;

import cmc.mellyserver.placeScrap.application.dto.PlaceScrapResponseDto;
import cmc.mellyserver.placeScrap.application.dto.ScrapedPlaceResponseDto;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.data.domain.Slice;

import java.util.List;

@Data
@AllArgsConstructor
public class PlaceScrapResponseWrapper {
    private List<PlaceScrapResponseDto> scrapCount;
}
