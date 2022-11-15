package cmc.mellyserver.user.presentation.dto.common;

import cmc.mellyserver.placeScrap.application.dto.ScrapedPlaceResponseDto;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.data.domain.Slice;

@Data
@AllArgsConstructor
public class ScrapPlaceResponseWrapper {
    private Slice<ScrapedPlaceResponseDto> scrapPlace;
}
