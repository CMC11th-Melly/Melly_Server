package cmc.mellyserver.place.application;

import cmc.mellyserver.common.exception.ExceptionCodeAndDetails;
import cmc.mellyserver.common.exception.GlobalBadRequestException;
import cmc.mellyserver.place.domain.Place;
import cmc.mellyserver.place.domain.PlaceQueryRepository;
import cmc.mellyserver.place.domain.PlaceRepository;
import cmc.mellyserver.place.domain.service.GetPlaceInfoDto;
import cmc.mellyserver.place.domain.service.PlaceDomainService;
import cmc.mellyserver.place.presentation.dto.PlaceAssembler;
import cmc.mellyserver.place.presentation.dto.PlaceGroupCond;
import cmc.mellyserver.place.presentation.dto.PlaceListReponseDto;
import cmc.mellyserver.user.domain.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class PlaceService {

    private final PlaceQueryRepository placeQueryRepository;
    private final PlaceRepository placeRepository;
    private final PlaceDomainService placeDomainService;
    private final EntityManager em;
    private final UserRepository userRepository;

    public GetPlaceInfoDto getPlaceInfo(Long placeId, User user)
    {
        return placeDomainService.getPlaceInfo(placeId, user.getUsername());
    }

    /**
     * 1. 일정 범위의 장소를 다 가져옴
     * 2. 여기서 중요한건 내가 쓴 메모리가 들어있는 장소만 가져오기
     */
    public List<PlaceListReponseDto> getPlaceList(String userId, PlaceGroupCond placeGroupCond)
    {
        cmc.mellyserver.user.domain.User user = userRepository.findUserByUserId(userId).orElseThrow(() -> {
            throw new GlobalBadRequestException(ExceptionCodeAndDetails.NO_SUCH_USER);
        });
        List<Place> placeUserMemoryExist = placeQueryRepository.getPlaceUserMemoryExist(user);
        return PlaceAssembler.placeListReponseDto(placeUserMemoryExist, placeGroupCond, userId);
    }

//    @Transactional(readOnly = true)
//    public List<Place> getPlaceList(Double wlatitude, Double wlongitude, Double elatitude, Double elongitude)
//    {
//
////        Location northEast = GeometryUtil
////                .calculate(latitude, longitude, distance, Direction.NORTHEAST.getBearing());
////        Location southWest = GeometryUtil
////                .calculate(latitude, longitude, distance, Direction.SOUTHWEST.getBearing());
//
//        double x1 = elatitude;
//        double y1 = elongitude;
//        double x2 = wlatitude;
//        double y2 = wlongitude;
//
//        String pointFormat = String.format("'LINESTRING(%f %f, %f %f)')", x1, y1, x2, y2);
//        Query query = em.createNativeQuery("SELECT p.id "
//                + "FROM place AS p "
//                + "WHERE MBRContains(ST_LINESTRINGFROMTEXT(" + pointFormat + ", p.point)", Place.class);
//
//        List<Place> places = query.getResultList();
//        return places;
//    }


    public void savePlace()
    {

    }

}
