package cmc.mellyserver.place.application;

import cmc.mellyserver.common.exception.ExceptionCodeAndDetails;
import cmc.mellyserver.common.exception.GlobalBadRequestException;
import cmc.mellyserver.group.domain.enums.GroupType;
import cmc.mellyserver.place.domain.Place;
import cmc.mellyserver.place.domain.PlaceQueryRepository;
import cmc.mellyserver.place.domain.service.GetPlaceInfoDto;
import cmc.mellyserver.place.domain.service.PlaceDomainService;
import cmc.mellyserver.place.presentation.dto.PlaceAssembler;
import cmc.mellyserver.place.presentation.dto.PlaceListReponseDto;
import cmc.mellyserver.user.domain.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class PlaceService {

    private final PlaceQueryRepository placeQueryRepository;
    private final PlaceDomainService placeDomainService;
    private final UserRepository userRepository;

    public GetPlaceInfoDto getPlaceInfo(Long placeId, User user)
    {
        return placeDomainService.getPlaceInfo(placeId, user.getUsername());
    }


    public List<PlaceListReponseDto> getPlaceList(String userId, GroupType groupType)
    {
        cmc.mellyserver.user.domain.User user = userRepository.findUserByUserId(userId).orElseThrow(() -> {
            throw new GlobalBadRequestException(ExceptionCodeAndDetails.NO_SUCH_USER);
        });
        List<Place> placeUserMemoryExist = placeQueryRepository.getPlaceUserMemoryExist(user);
        return PlaceAssembler.placeListReponseDto(placeUserMemoryExist, groupType, userId);
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
