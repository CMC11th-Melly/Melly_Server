package cmc.mellyserver.place.application;


import cmc.mellyserver.common.util.auth.AuthenticatedUserChecker;
import cmc.mellyserver.group.domain.enums.GroupType;
import cmc.mellyserver.place.application.dto.PlaceResponseDto;
import cmc.mellyserver.place.domain.Place;
import cmc.mellyserver.place.domain.PlaceQueryRepository;
import cmc.mellyserver.place.domain.service.PlaceDomainService;
import cmc.mellyserver.place.presentation.dto.PlaceAssembler;
import cmc.mellyserver.place.presentation.dto.PlaceListReponseDto;
import cmc.mellyserver.user.domain.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;


@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class PlaceService {

    private final PlaceQueryRepository placeQueryRepository;
    private final PlaceDomainService placeDomainService;
    private final AuthenticatedUserChecker authenticatedUserChecker;


    /**
     * 지도 상에 로그인 유저가 메모리를 등록한 장소들 표시
     */
    public List<PlaceListReponseDto> getMarkerPosition(String uid, GroupType groupType)
    {
        User user = authenticatedUserChecker.checkAuthenticatedUserExist(uid);
        List<Place> placeUserMemoryExist = placeQueryRepository.getPlaceUserMemoryExist(user);
        return PlaceAssembler.placeListReponseDto(placeUserMemoryExist, groupType, uid);
    }

    /**
     * 좌표를 기준으로 장소 조회
     */
    public PlaceResponseDto getPlaceByPosition(String uid, Double lat, Double lng)
    {
        return placeDomainService.getPlace(uid, lat, lng);
    }

    /**
     * PlaceId로 특정 장소 검색
     */
    public PlaceResponseDto placeSearchByMemory(String uid,Long placeId) {

        User user = authenticatedUserChecker.checkAuthenticatedUserExist(uid);
        Place placeByMemory = placeQueryRepository.getPlaceByMemory(placeId);
        placeByMemory.setScraped(checkIsScraped(user,placeByMemory));
        return PlaceAssembler.placeResponseDto(placeByMemory,user);

    }

    private boolean checkIsScraped(User user, Place place)
    {
       return user.getScraps().stream().anyMatch(s -> s.getPlace().getId().equals(place.getId()));
    }
}
