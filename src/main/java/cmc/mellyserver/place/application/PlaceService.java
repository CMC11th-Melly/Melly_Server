package cmc.mellyserver.place.application;


import cmc.mellyserver.common.enums.GroupType;
import cmc.mellyserver.memory.domain.MemoryQueryRepository;
import cmc.mellyserver.place.application.dto.PlaceResponseDto;
import cmc.mellyserver.place.domain.Place;
import cmc.mellyserver.place.domain.Position;
import cmc.mellyserver.place.domain.repository.PlaceQueryRepository;
import cmc.mellyserver.place.domain.repository.PlaceRepository;
import cmc.mellyserver.scrap.domain.PlaceScrapQueryRepository;
import cmc.mellyserver.place.presentation.dto.PlaceAssembler;
import cmc.mellyserver.place.presentation.dto.response.PlaceListReponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Optional;


@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class PlaceService {

    private final PlaceQueryRepository placeQueryRepository;
    private final PlaceRepository placeRepository;
    private final PlaceScrapQueryRepository placeScrapQueryRepository;
    private final MemoryQueryRepository memoryQueryRepository;


    /**
     * 지도 상에 로그인 유저가 메모리를 등록한 장소들 표시
     */
    public List<PlaceListReponseDto> displayMarker(Long userSeq, GroupType groupType)
    {
        List<Place> placeUserMemoryExist = placeQueryRepository.getPlaceUserMemoryExist(userSeq);
        return PlaceAssembler.placeListReponseDto(placeUserMemoryExist, groupType, userSeq);
    }

    /**
     * PlaceId로 특정 장소 검색(당장은 최적화 완료,인덱스 설정하기)
     */
    public PlaceResponseDto searchPlaceByPlaceId(Long userSeq,Long placeId)
    {
        Place place = placeQueryRepository.searchPlaceByPlaceId(placeId);
        HashMap<String, Long> memoryCounts = memoryQueryRepository.countMemoriesBelongToPlace(userSeq, placeId);
        Boolean checkCurrentLoginUserScraped = placeScrapQueryRepository.checkCurrentLoginUserScrapedPlaceByPlaceId(placeId, userSeq);
        return PlaceAssembler.placeResponseDto(place,checkCurrentLoginUserScraped,memoryCounts);
    }

    public PlaceResponseDto searchPlaceByPosition(Long userSeq, Double lat, Double lng)
    {
        Optional<Place> optPlace = placeRepository.findPlaceByPosition(new Position(lat, lng));

        if(optPlace.isEmpty()) {
            return PlaceResponseDto.PlaceNotCreated(new Position(lat, lng), 0L, 0L, false);
        }

        Place place = optPlace.get();

        HashMap<String, Long> memoryCounts = memoryQueryRepository.countMemoryOfPlace(place.getId(), userSeq);
        Boolean isCurrentLoginUserScraped = placeScrapQueryRepository.checkCurrentLoginUserScrapedPlaceByPosition(userSeq, new Position(lat, lng));

        return new PlaceResponseDto(
                place.getId(), // id
                place.getPosition(), // 좌표
                memoryCounts.get("myMemoryCount"), // 내가 쓴 메모리 개수
                memoryCounts.get("otherMemoryCount"), // 상대방이 쓴 메모리 개수
                isCurrentLoginUserScraped, // 해당 장소 스크랩 여부
                place.getPlaceCategory(), // 해당 장소 카테고리
                place.getPlaceName(), // 장소 이름
                GroupType.ALL, // 해당 장소 타입
                place.getPlaceImage() // 장소 이미지
        );
    }
}
