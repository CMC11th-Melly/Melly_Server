package cmc.mellyserver.scrap.application;

import cmc.mellyserver.common.enums.ScrapType;
import cmc.mellyserver.scrap.application.dto.request.CreatePlaceScrapRequestDto;
import cmc.mellyserver.scrap.application.dto.response.PlaceScrapCountResponseDto;
import cmc.mellyserver.scrap.application.dto.response.ScrapedPlaceResponseDto;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import java.util.List;

public interface PlaceScrapService {

    Slice<ScrapedPlaceResponseDto> findScrapedPlace(Pageable pageable, Long loginUserSeq, ScrapType scrapType);

    List<PlaceScrapCountResponseDto> countByPlaceScrapType(Long loginUserSeq);

    void createScrap(CreatePlaceScrapRequestDto createPlaceScrapRequestDto);

    void removeScrap(Long loginUserSeq, Double lat, Double lng);
}
