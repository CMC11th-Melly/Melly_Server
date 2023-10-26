package cmc.mellyserver.domain.scrap;

import cmc.mellyserver.dbcore.place.Position;
import cmc.mellyserver.dbcore.scrap.PlaceScrap;
import cmc.mellyserver.dbcore.scrap.PlaceScrapRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class PlaceScrapWriter {

	private final PlaceScrapRepository placeScrapRepository;

	public PlaceScrap save(PlaceScrap placeScrap) {
		return placeScrapRepository.save(placeScrap);
	}

	public void deleteByUserIdAndPlacePosition(Long userId, Position position) {
		placeScrapRepository.deleteByUserIdAndPlacePosition(userId, position);
	}

}
