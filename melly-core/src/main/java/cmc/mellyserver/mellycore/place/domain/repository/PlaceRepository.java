package cmc.mellyserver.mellycore.place.domain.repository;

import cmc.mellyserver.mellycore.common.exception.BusinessException;
import cmc.mellyserver.mellycore.common.exception.ErrorCode;
import cmc.mellyserver.mellycore.place.domain.Place;
import cmc.mellyserver.mellycore.place.domain.Position;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PlaceRepository extends JpaRepository<Place, Long> {

    Optional<Place> findPlaceByPosition(Position position);

    Optional<Place> findAllByPosition(Position position);

    default Place getByPosition(Position position) {

        return findPlaceByPosition(position)
                .orElseThrow(() -> {
                    throw new BusinessException(ErrorCode.NO_SUCH_PLACE);
                });
    }

}
