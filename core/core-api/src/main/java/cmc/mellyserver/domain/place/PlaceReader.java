package cmc.mellyserver.domain.place;

import cmc.mellyserver.dbcore.place.Place;
import cmc.mellyserver.dbcore.place.PlaceRepository;
import cmc.mellyserver.dbcore.place.Position;
import cmc.mellyserver.support.exception.BusinessException;
import cmc.mellyserver.support.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class PlaceReader {

    private final PlaceRepository placeRepository;

    public Place findByPosition(Position position) {
        return placeRepository.findByPosition(position).orElseThrow(() -> new BusinessException(ErrorCode.NO_SUCH_PLACE));
    }


}
