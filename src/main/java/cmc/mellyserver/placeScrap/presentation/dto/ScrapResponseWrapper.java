package cmc.mellyserver.placeScrap.presentation.dto;

import cmc.mellyserver.placeScrap.application.dto.ScrapedPlaceResponseDto;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.data.domain.Slice;

@AllArgsConstructor
@Data
public class ScrapResponseWrapper {

    private Slice<ScrapedPlaceResponseDto> scrapedPlace;
}
