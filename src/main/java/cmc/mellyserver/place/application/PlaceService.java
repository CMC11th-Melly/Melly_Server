package cmc.mellyserver.place.application;

import cmc.mellyserver.place.domain.PlaceQueryRepository;
import cmc.mellyserver.place.domain.PlaceRepository;
import cmc.mellyserver.place.domain.service.PlaceDomainService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class PlaceService {

    private final PlaceQueryRepository placeQueryRepository;
    private final PlaceRepository placeRepository;
    private final PlaceDomainService placeDomainService;

    public void getPlaceInfo(Long placeId, User user)
    {
         placeDomainService.getPlaceInfo(placeId, user.getUsername());
    }

    public void savePlace()
    {

    }

}
