package cmc.mellyserver.domain.scrap;

import cmc.mellyserver.dbcore.place.Position;
import cmc.mellyserver.dbcore.scrap.PlaceScrap;
import cmc.mellyserver.dbcore.scrap.PlaceScrapRepository;
import cmc.mellyserver.dbcore.scrap.enums.ScrapType;
import cmc.mellyserver.domain.scrap.query.PlaceScrapQueryRepository;
import cmc.mellyserver.domain.scrap.query.dto.PlaceScrapCountResponseDto;
import cmc.mellyserver.domain.scrap.query.dto.ScrapedPlaceResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class PlaceScrapReader {

    private final PlaceScrapRepository placeScrapRepository;

    private final PlaceScrapQueryRepository placeScrapQueryRepository;

    public Optional<PlaceScrap> findByUserIdAndPlaceId(Long userId, Long placeId) {
        return placeScrapRepository.findByUserIdAndPlaceId(userId, placeId);
    }

    public Slice<ScrapedPlaceResponseDto> getUserScrapedPlace(Long lastId, Pageable pageable, Long userId, ScrapType scrapType) {
        return placeScrapQueryRepository.getUserScrapedPlace(lastId, pageable, userId, scrapType);
    }

    public List<PlaceScrapCountResponseDto> getScrapedPlaceGrouping(Long userId) {
        return placeScrapQueryRepository.getScrapedPlaceGrouping(userId);
    }

    public boolean checkUserScraped(Long userId, Long placeId) {
        return placeScrapQueryRepository.checkCurrentLoginUserScrapedPlaceByPlaceId(placeId, userId);
    }

    public boolean checkUserScraped(Long userId, Position position) {
        return placeScrapQueryRepository.checkCurrentLoginUserScrapedPlaceByPosition(userId, position);
    }

}
