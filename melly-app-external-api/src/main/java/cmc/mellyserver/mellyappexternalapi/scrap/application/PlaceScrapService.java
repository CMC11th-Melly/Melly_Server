package cmc.mellyserver.mellyappexternalapi.scrap.application;

import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;

import cmc.mellyserver.mellyappexternalapi.scrap.application.dto.request.CreatePlaceScrapRequestDto;
import cmc.mellyserver.mellydomain.common.enums.ScrapType;
import cmc.mellyserver.mellydomain.scrap.domain.repository.dto.PlaceScrapCountResponseDto;
import cmc.mellyserver.mellydomain.scrap.domain.repository.dto.ScrapedPlaceResponseDto;

public interface PlaceScrapService {

	Slice<ScrapedPlaceResponseDto> findScrapedPlace(Pageable pageable, Long loginUserSeq, ScrapType scrapType);

	List<PlaceScrapCountResponseDto> countByPlaceScrapType(Long loginUserSeq);

	void createScrap(CreatePlaceScrapRequestDto createPlaceScrapRequestDto);

	void removeScrap(Long loginUserSeq, Double lat, Double lng);
}
