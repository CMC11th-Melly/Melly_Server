package cmc.mellyserver.domain.place;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Component;

import cmc.mellyserver.dbcore.group.GroupType;
import cmc.mellyserver.dbcore.place.Place;
import cmc.mellyserver.dbcore.place.PlaceRepository;
import cmc.mellyserver.dbcore.place.Position;
import cmc.mellyserver.domain.memory.query.dto.FindPlaceByMemoryTitleResponseDto;
import cmc.mellyserver.domain.place.query.PlaceQueryRepository;
import cmc.mellyserver.domain.scrap.dto.MarkedPlaceResponseDto;
import cmc.mellyserver.support.exception.BusinessException;
import cmc.mellyserver.support.exception.ErrorCode;
import lombok.RequiredArgsConstructor;

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
        return placeRepository.findByPosition(position)
            .orElseThrow(() -> new BusinessException(ErrorCode.NO_SUCH_PLACE));
    }

    public List<MarkedPlaceResponseDto> placeUserMemoryExist(Long userId, GroupType groupType) {
        List<Place> placeUserMemoryExist = placeQueryRepository.getPlaceUserMemoryExist(userId, groupType);
        return placeUserMemoryExist.stream()
            .map(each -> new MarkedPlaceResponseDto(each.getPosition(), null, each.getId(), 0L))
            .collect(Collectors.toList());
    }

    public List<FindPlaceByMemoryTitleResponseDto> findByMemoryTitle(Long userId, String memoryName) {
        return placeQueryRepository.searchPlaceByContainMemoryName(userId, memoryName);
    }

}
