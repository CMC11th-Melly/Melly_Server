package cmc.mellyserver.mellycore.place.domain.repository;

import cmc.mellyserver.mellycore.place.domain.Place;
import cmc.mellyserver.mellycore.place.domain.Position;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PlaceRepository extends JpaRepository<Place, Long> {

    Place findPlaceByPosition(Position position);

    Optional<Place> findAllByPosition(Position position);

}
