package cmc.mellyserver.place.domain.repository;

import cmc.mellyserver.place.domain.Place;
import cmc.mellyserver.place.domain.Position;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;


public interface PlaceRepository extends JpaRepository<Place,Long> {

    Optional<Place> findPlaceByPosition(Position position);
    Optional<Place> findPlaceByPlaceName(String placeName);
}
