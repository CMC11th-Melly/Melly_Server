package cmc.mellyserver.domain.scrap;

import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Component;

import cmc.mellyserver.dbcore.scrap.ScrapType;
import cmc.mellyserver.domain.scrap.query.PlaceScrapQueryRepository;
import cmc.mellyserver.domain.scrap.query.dto.PlaceScrapCountResponseDto;
import cmc.mellyserver.domain.scrap.query.dto.ScrapedPlaceResponseDto;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class PlaceScrapReader {

  private final PlaceScrapQueryRepository placeScrapQueryRepository;

  public Slice<ScrapedPlaceResponseDto> getUserScrapedPlace(Long lastId, Pageable pageable, Long userId,
	  ScrapType scrapType) {
	return placeScrapQueryRepository.getUserScrapedPlace(lastId, pageable, userId, scrapType);
  }

  public List<PlaceScrapCountResponseDto> getScrapedPlaceGrouping(Long userId) {
	return placeScrapQueryRepository.getScrapedPlaceGrouping(userId);
  }

}
