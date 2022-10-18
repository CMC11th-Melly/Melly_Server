package cmc.mellyserver.scrap.presentation.dto;

import cmc.mellyserver.scrap.application.dto.ScrapedPlaceResponseDto;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.data.domain.Slice;

import java.util.List;

@AllArgsConstructor
@Data
public class ScrapResponseWrapper {

    private Slice<ScrapedPlaceResponseDto> scrapedPlace;
}
