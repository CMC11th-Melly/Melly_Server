package cmc.mellyserver.mellycore.place.domain.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import cmc.mellyserver.mellycore.place.domain.Place;
import cmc.mellyserver.mellycore.place.domain.Position;

public interface PlaceRepository extends JpaRepository<Place, Long> {

	Optional<Place> findPlaceByPosition(Position position);

}
