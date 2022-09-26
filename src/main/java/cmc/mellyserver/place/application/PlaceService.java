package cmc.mellyserver.place.application;

import cmc.mellyserver.place.domain.Place;
import cmc.mellyserver.place.domain.PlaceQueryRepository;
import cmc.mellyserver.place.domain.PlaceRepository;
import cmc.mellyserver.place.domain.service.PlaceDomainService;
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

    public void getPlaceInfo(Long placeId, User user)
    {
         placeDomainService.getPlaceInfo(placeId, user.getUsername());
    }
    @Transactional(readOnly = true)
    public List<Place> getPlaceList(Double wlatitude, Double wlongitude, Double elatitude, Double elongitude)
    {

//        Location northEast = GeometryUtil
//                .calculate(latitude, longitude, distance, Direction.NORTHEAST.getBearing());
//        Location southWest = GeometryUtil
//                .calculate(latitude, longitude, distance, Direction.SOUTHWEST.getBearing());

        double x1 = elatitude;
        double y1 = elongitude;
        double x2 = wlatitude;
        double y2 = wlongitude;

        String pointFormat = String.format("'LINESTRING(%f %f, %f %f)')", x1, y1, x2, y2);
        Query query = em.createNativeQuery("SELECT p.id "
                + "FROM place AS p "
                + "WHERE MBRContains(ST_LINESTRINGFROMTEXT(" + pointFormat + ", p.point)", Place.class);

        List<Place> places = query.getResultList();
        return places;
    }


    public void savePlace()
    {

    }

}
