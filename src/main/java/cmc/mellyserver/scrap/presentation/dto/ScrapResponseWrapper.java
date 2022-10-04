package cmc.mellyserver.scrap.presentation.dto;

import cmc.mellyserver.scrap.application.dto.ScrapedPlaceResponseDto;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@AllArgsConstructor
@Data
public class ScrapResponseWrapper {

    private List<ScrapedPlaceResponseDto> scrapedPlace;
}
