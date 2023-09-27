package cmc.mellyserver.mellyapi.domain.place;

import cmc.mellyserver.dbcore.group.enums.GroupType;
import cmc.mellyserver.dbcore.place.Place;
import cmc.mellyserver.dbcore.place.PlaceRepository;
import cmc.mellyserver.dbcore.place.Position;
import cmc.mellyserver.mellyapi.domain.memory.query.MemoryQueryRepository;
import cmc.mellyserver.mellyapi.domain.memory.query.dto.FindPlaceInfoByMemoryNameResponseDto;
import cmc.mellyserver.mellyapi.domain.place.query.PlaceQueryRepository;
import cmc.mellyserver.mellyapi.domain.scrap.dto.MarkedPlaceResponseDto;
import cmc.mellyserver.mellyapi.domain.scrap.dto.PlaceResponseDto;
import cmc.mellyserver.mellyapi.domain.scrap.query.PlaceScrapQueryRepository;
import cmc.mellyserver.mellyapi.support.exception.BusinessException;
import cmc.mellyserver.mellyapi.support.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PlaceService {

    private final PlaceQueryRepository placeQueryRepository;

    private final PlaceRepository placeRepository;

    private final PlaceScrapQueryRepository placeScrapQueryRepository;

    private final MemoryQueryRepository memoryQueryRepository;

    /*
    좌표 기반 검색으로 개선 필요
     */
    @Transactional(readOnly = true)
    public List<MarkedPlaceResponseDto> displayMarkedPlace(Long id, GroupType groupType) {

        List<Place> placeUserMemoryExist = placeQueryRepository.getPlaceUserMemoryExist(id, groupType);
        return placeUserMemoryExist.stream().map(each -> new MarkedPlaceResponseDto(each.getPosition(), null, each.getId(), null))
                .collect(Collectors.toList());
    }

    /*
    캐싱 적용 여부 : 불가능
    이유 : 반환 DTO에 다른 유저가 작성한 메모리 수가 포함된다. 유저가 대규모로 유입되면 데이터 갱신이 자주 발생하여 캐시 효율성이 적다고 판단
     */
    @Transactional(readOnly = true)
    public PlaceResponseDto findPlaceByPlaceId(Long id, Long placeId) {

        Place place = placeRepository.findById(placeId).orElseThrow(() -> {
            throw new BusinessException(ErrorCode.NO_SUCH_PLACE);
        });
        HashMap<String, Long> memoryCounts = memoryQueryRepository.countMemoriesBelongToPlace(id, placeId);
        Boolean checkCurrentLoginUserScraped = placeScrapQueryRepository.checkCurrentLoginUserScrapedPlaceByPlaceId(placeId, id);

        return new PlaceResponseDto(place.getId(), place.getPosition(),
                memoryCounts.get("belongToUSer"),
                memoryCounts.get("NotBelongToUSer"),
                checkCurrentLoginUserScraped, place.getPlaceCategory(), place.getPlaceName(),
                null,
                place.getPlaceImage());
    }

    /*
    캐싱 적용 여부 : 불가능
    이유 : 반환 DTO에 다른 유저가 작성한 메모리 수가 포함된다. 유저가 대규모로 유입되면 데이터 갱신이 자주 발생하여 캐시 효율성이 적다고 판단
     */
    @Transactional(readOnly = true)
    public PlaceResponseDto findPlaceByPosition(Long id, Double lat, Double lng) {

        Optional<Place> optPlace = placeRepository.findAllByPosition(new Position(lat, lng));

        if (optPlace.isEmpty()) {
            return PlaceResponseDto.PlaceNotCreated(new Position(lat, lng), 0L, 0L, false);
        }
        Place place = optPlace.get();

        HashMap<String, Long> memoryCounts = memoryQueryRepository.countMemoriesBelongToPlace(
                place.getId(), id);
        Boolean isCurrentLoginUserScraped = placeScrapQueryRepository.checkCurrentLoginUserScrapedPlaceByPosition(
                id, new Position(lat, lng));

        return new PlaceResponseDto(place.getId(), place.getPosition(), memoryCounts.get("belongToUser"), memoryCounts.get("notBelongToUser"),
                isCurrentLoginUserScraped, place.getPlaceCategory(), place.getPlaceName(), null, place.getPlaceImage());
    }

    /*
    캐시 적용 여부 : 가능
     */
    @Transactional(readOnly = true)
    public List<FindPlaceInfoByMemoryNameResponseDto> findPlaceInfoByMemoryName(Long id, String memoryName) {

        return placeQueryRepository.searchPlaceByContainMemoryName(id, memoryName);
    }
}
