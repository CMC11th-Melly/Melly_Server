package cmc.mellyserver.place.domain.service;

import cmc.mellyserver.common.util.auth.AuthenticatedUserChecker;
import cmc.mellyserver.group.domain.enums.GroupType;
import cmc.mellyserver.memory.domain.enums.OpenType;
import cmc.mellyserver.place.application.dto.PlaceResponseDto;
import cmc.mellyserver.place.domain.Place;
import cmc.mellyserver.place.domain.PlaceRepository;
import cmc.mellyserver.place.domain.Position;
import cmc.mellyserver.user.domain.User;
import cmc.mellyserver.user.domain.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class PlaceDomainService {

    private final PlaceRepository placeRepository;
    private final AuthenticatedUserChecker authenticatedUserChecker;

    public PlaceResponseDto getPlaceByPosition(String uid, Double lat, Double lng)
    {

        // 1. 위도 경도로 데이터 가져오기
        Optional<Place> placeByPosition = placeRepository.findPlaceByPosition(new Position(lat, lng));
        // 2. 만약 아무도 데이터 저장 안했다면?
        if(placeByPosition.isEmpty())
        {
            return PlaceResponseDto.PlaceNotCreated(new Position(lat,lng),0L,0L,false);
        }
        // 3. 만약 장소가 존재하면?
        Place place = placeByPosition.get();
        User user = authenticatedUserChecker.checkAuthenticatedUserExist(uid);

        place.setScraped(checkIsScraped(user,place));

        long myMemoryCount = place.getMemories()
                .stream()
                .filter(m -> m.getUser().getUserId().equals(user.getUserId()))
                .count();

        long otherMemoryCount = place.getMemories()
                .stream()
                .filter(m -> (!m.getUser().getUserId().equals(user.getUserId())) & m.getOpenType().equals(OpenType.ALL) )
                .count();

        return new PlaceResponseDto(place.getId(),place.getPosition(),myMemoryCount,otherMemoryCount,place.getIsScraped(),place.getPlaceCategory(), place.getPlaceName(), GroupType.ALL,place.getPlaceImage());


    }

    private boolean checkIsScraped(User user, Place place)
    {
        return user.getPlaceScraps().stream().anyMatch(s -> s.getPlace().getId().equals(place.getId()));
    }

}
