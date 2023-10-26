package cmc.mellyserver.dbcore.scrap;

import cmc.mellyserver.dbcore.place.Position;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PlaceScrapRepository extends JpaRepository<PlaceScrap, Long> {

	Optional<PlaceScrap> findByUserIdAndPlaceId(Long id, Long placeId);

	void deleteByUserIdAndPlacePosition(Long id, Position position);

}