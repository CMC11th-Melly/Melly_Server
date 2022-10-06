package cmc.mellyserver.place.domain.service;

import cmc.mellyserver.common.exception.ExceptionCodeAndDetails;
import cmc.mellyserver.common.exception.GlobalBadRequestException;
import cmc.mellyserver.common.util.AuthenticatedUserChecker;
import cmc.mellyserver.memory.domain.enums.OpenType;
import cmc.mellyserver.place.domain.Place;
import cmc.mellyserver.place.domain.PlaceRepository;
import cmc.mellyserver.place.domain.Position;
import cmc.mellyserver.place.domain.service.dto.*;
import cmc.mellyserver.scrap.domain.Scrap;
import cmc.mellyserver.user.domain.User;
import cmc.mellyserver.user.domain.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PlaceDomainService {

    private final PlaceRepository placeRepository;
    private final UserRepository userRepository;
    private final AuthenticatedUserChecker authenticatedUserChecker;
    // 1. 장소 클릭
    // 2. 아무도 메모리를 작성하지 않은 장소
    // 3. 가게 이름 뜨고, 개수 0개, 스크랩 여부 x
    public GetPlaceInfoDto getPlaceInfo(Long placeId, String userId)
    {

        User user = userRepository.findUserByUserId(userId).orElseThrow(() -> {
            throw new GlobalBadRequestException(ExceptionCodeAndDetails.NO_SUCH_USER);
        });

        Place place = placeRepository.findById(placeId).orElseThrow(() -> {
            throw new GlobalBadRequestException(ExceptionCodeAndDetails.NO_SUCH_PLACE);
        });

        List<MyMemoryDto> myMemoryDtos = place.getMemories()
                .stream()
                .filter(m -> m.getUser().getUserId().equals(user.getUserId()))
                .map(ml -> new MyMemoryDto(ml.getId(),ml.getGroupInfo().getGroupType(),
                        ml.getMemoryImages().stream().map(mi ->
                                new MemoryImageDto(mi.getId(),
                                        mi.getImagePath()))
                                .collect(Collectors.toList()),
                        ml.getTitle(),
                        ml.getKeyword(),
                        ml.getCreatedDate().toString()))
                .collect(Collectors.toList());

        List<OtherMemoryDto> otherMemoryDtos = place.getMemories()
                .stream()
                .filter(m -> (!m.getUser().getUserId().equals(user.getUserId())) & m.getOpenType().equals(OpenType.ALL) )
                .map(ml -> new OtherMemoryDto(ml.getId(),ml.getGroupInfo().getGroupType(),
                        ml.getMemoryImages().stream().map(mi ->
                                new MemoryImageDto(mi.getId(),
                                        mi.getImagePath()))
                                .collect(Collectors.toList()),
                        ml.getTitle(),
                        ml.getKeyword(),
                        ml.getCreatedDate().toString()))
                .collect(Collectors.toList());

        return new GetPlaceInfoDto(place.getName(),false,place.getPlaceImage(),myMemoryDtos,otherMemoryDtos);
    }

    /*
    CASE 1 : 아직 아무도 스크랩도 안하고 메모리 저장도 안함.
    CASE 2 : 그 장소에 대한 데이터는 존재!
     */

    public GetPlaceInfo getPlace(String uid, Double lat, Double lng)
    {
        // 먼저 그 장소가 존재하는지 체크
        Optional<Place> placeByPosition = placeRepository.findPlaceByPosition(new Position(lat, lng));
        if(placeByPosition.isEmpty())
        {
            return new GetPlaceInfo(false,0L,0L);
        }
        Place place = placeByPosition.get();
        User user = authenticatedUserChecker.checkAuthenticatedUserExist(uid);
        List<Scrap> scraps = user.getScraps();
        boolean flag = false;
        if(!scraps.isEmpty())
        {
            // 유저가 가지고 있던 scrap
            for(Scrap scrap : scraps)
            {
                Long placeId = scrap.getPlace().getId();
                if(place.getScraps().stream().anyMatch(s -> s.getPlace().getId().equals(placeId)))
                {
                    place.setScraped(true);
                    System.out.println("일치함!");
                    break;
                }
            }

        }
        long myMemoryCount = place.getMemories()
                .stream()
                .filter(m -> m.getUser().getUserId().equals(user.getUserId()))
                .count();

        long otherMemoryCount = place.getMemories()
                .stream()
                .filter(m -> (!m.getUser().getUserId().equals(user.getUserId())) & m.getOpenType().equals(OpenType.ALL) )
                .count();

        return new GetPlaceInfo(place.getIsScraped(),myMemoryCount,otherMemoryCount);


    }


}
