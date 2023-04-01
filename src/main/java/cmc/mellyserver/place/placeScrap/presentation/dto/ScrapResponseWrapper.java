package cmc.mellyserver.place.placeScrap.presentation.dto;

import cmc.mellyserver.place.placeScrap.application.dto.ScrapedPlaceResponseDto;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.data.domain.Slice;

@AllArgsConstructor
@Data
public class ScrapResponseWrapper {

    private Slice<ScrapedPlaceResponseDto> scrapedPlace;
}
