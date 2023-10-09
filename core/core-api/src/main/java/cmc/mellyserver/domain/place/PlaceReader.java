package cmc.mellyserver.domain.place;

import cmc.mellyserver.dbcore.group.enums.GroupType;
import cmc.mellyserver.dbcore.place.Place;
import cmc.mellyserver.dbcore.place.PlaceRepository;
import cmc.mellyserver.dbcore.place.Position;
import cmc.mellyserver.domain.memory.query.dto.FindPlaceInfoByMemoryNameResponseDto;
import cmc.mellyserver.domain.place.query.PlaceQueryRepository;
import cmc.mellyserver.support.exception.BusinessException;
import cmc.mellyserver.support.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class PlaceReader {

    private final PlaceRepository placeRepository;

    private final PlaceQueryRepository placeQueryRepository;

    public Place findById(Long placeId) {
        return placeRepository.findById(placeId).orElseThrow(() -> {
            throw new BusinessException(ErrorCode.NO_SUCH_PLACE);
        });
    }

    public Place findByPosition(Position position) {
        return placeRepository.findByPosition(position).orElseThrow(() -> new BusinessException(ErrorCode.NO_SUCH_PLACE));
    }

    public List<Place> placeUserMemoryExist(Long userId, GroupType groupType) {
        return placeQueryRepository.getPlaceUserMemoryExist(userId, groupType);
    }

    public List<FindPlaceInfoByMemoryNameResponseDto> findByMemoryTitle(Long id, String memoryName) {
        return placeQueryRepository.searchPlaceByContainMemoryName(id, memoryName);
    }


}
