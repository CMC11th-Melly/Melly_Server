package cmc.mellyserver.dbcore.scrap;

import cmc.mellyserver.dbcore.place.Position;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PlaceScrapRepository extends JpaRepository<PlaceScrap, Long> {

	boolean existsByUserIdAndPlaceId(Long id, Long placeId);

	void deleteByUserIdAndPlacePosition(Long id, Position position);

}