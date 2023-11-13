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
		return placeScrapQueryRepository.checkCurrentLoginUserScrapedPlaceByPlaceId(placeId, userId);
	}

	public boolean check(Long userId, Position position) {
		return placeScrapQueryRepository.checkCurrentLoginUserScrapedPlaceByPosition(userId, position);
	}
}
