package cmc.mellyserver.place.domain;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;


public interface PlaceRepository extends JpaRepository<Place,Long> {

    Optional<Place> findPlaceByPosition(Position position);
    Optional<Place> findPlaceByPlaceName(String placeName);
}
