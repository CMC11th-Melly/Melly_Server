package cmc.mellyserver.place.application;


import cmc.mellyserver.common.util.auth.AuthenticatedUserChecker;
import cmc.mellyserver.group.domain.enums.GroupType;
import cmc.mellyserver.memory.domain.MemoryQueryRepository;
import cmc.mellyserver.place.application.dto.PlaceResponseDto;
import cmc.mellyserver.place.domain.Place;
import cmc.mellyserver.place.domain.PlaceQueryRepository;
import cmc.mellyserver.place.domain.service.PlaceDomainService;
import cmc.mellyserver.place.placeScrap.domain.PlaceScrap;
import cmc.mellyserver.place.placeScrap.domain.PlaceScrapRepository;
import cmc.mellyserver.place.presentation.dto.PlaceAssembler;
import cmc.mellyserver.place.presentation.dto.PlaceListReponseDto;
import cmc.mellyserver.user.domain.User;
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
    private final PlaceDomainService placeDomainService;
    private final PlaceScrapRepository placeScrapRepository;
    private final MemoryQueryRepository memoryQueryRepository;


    /**
     * 지도 상에 로그인 유저가 메모리를 등록한 장소들 표시
     */
    public List<PlaceListReponseDto> getMarkerPosition(Long userSeq, GroupType groupType)
    {
        // 장소 중에서 사용자가 작성한 메모리가 존재하는 장소 찾기
        List<Place> placeUserMemoryExist = placeQueryRepository.getPlaceUserMemoryExist(userSeq);
        // 변환
        return PlaceAssembler.placeListReponseDto(placeUserMemoryExist, groupType, userSeq);
    }



    /**
     * 좌표를 기준으로 장소 조회 (당장은 최적화 완료)
     * TODO : 코드 수정 완료 (2023.3.30)
     */
    public PlaceResponseDto getPlaceByPosition(Long userSeq, Double lat, Double lng)
    {
        return placeDomainService.getPlaceByPosition(userSeq, lat, lng);
    }



    /**
     * PlaceId로 특정 장소 검색(당장은 최적화 완료,인덱스 설정하기)
     * TODO : 코드 수정 완료 (2023.3.30)
     */
    public PlaceResponseDto searchPlaceByPlaceId(Long userSeq,Long placeId) {

        // 1. 원하는 장소를 ID 값으로 찾기
        Place place = placeQueryRepository.searchPlaceByPlaceId(placeId);

        // 2. 해당 장소에 속한 메모리 중, 내가 작성한 것과 다른 사람들이 작성한 것 개수 세기
        HashMap<String, Long> memoryCounts = memoryQueryRepository.countMemoriesBelongToPlace(userSeq, placeId);

        // 3. 로그인 유저가 스크랩 한 내용이 있는지 체크
        Optional<PlaceScrap> placeScrap = placeScrapRepository.findByPlaceIdAndUserUserSeq(placeId, userSeq);

        // 4. 만약 스크랩 존재하면 isScraped는 true
        if(placeScrap.isPresent())
        {
            return PlaceAssembler.placeResponseDto(place,true,memoryCounts);
        }

        // 5. 만약 없다면 isScraped는 false
        return PlaceAssembler.placeResponseDto(place,false,memoryCounts);

    }

}
