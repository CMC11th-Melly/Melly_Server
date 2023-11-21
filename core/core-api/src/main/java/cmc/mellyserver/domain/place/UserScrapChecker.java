package cmc.mellyserver.domain.place;

import org.springframework.stereotype.Component;

import cmc.mellyserver.dbcore.place.Position;
import cmc.mellyserver.domain.scrap.query.PlaceScrapQueryRepository;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class UserScrapChecker {

    private final PlaceScrapQueryRepository placeScrapQueryRepository;

    public boolean check(Long userId, Long placeId) {
        return placeScrapQueryRepository.checkUserScrapedPlaceByPlaceId(userId, placeId);
    }

    public boolean check(Long userId, Position position) {
        return placeScrapQueryRepository.checkUserScrapedPlaceByPosition(userId, position);
    }
}
