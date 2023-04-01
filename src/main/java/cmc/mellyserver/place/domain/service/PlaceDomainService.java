package cmc.mellyserver.place.domain.service;

import cmc.mellyserver.common.util.auth.AuthenticatedUserChecker;
import cmc.mellyserver.group.domain.enums.GroupType;
import cmc.mellyserver.memory.domain.MemoryQueryRepository;
import cmc.mellyserver.place.application.dto.PlaceResponseDto;
import cmc.mellyserver.place.domain.Place;
import cmc.mellyserver.place.domain.PlaceRepository;
import cmc.mellyserver.place.domain.Position;
import cmc.mellyserver.user.domain.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class PlaceDomainService {

    private final PlaceRepository placeRepository;
    private final MemoryQueryRepository memoryQueryRepository;
    private final AuthenticatedUserChecker authenticatedUserChecker;

    // & m.getOpenType().equals(OpenType.ALL)
    public PlaceResponseDto getPlaceByPosition(Long userSeq, Double lat, Double lng)
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

        HashMap<String, Long> memoryCounts = memoryQueryRepository.countMemoryOfPlace(place.getId(), userSeq);


        // 4. 가지고 오는 장소가 스크랩 되었는지 체크
//        place.setScraped(checkIsScraped(user,place));
//
//        long myMemoryCount = place.getMemories()
//                .stream()
//                .filter(m -> m.getUser().getUserSeq().equals(userSeq))
//                .count();
//
//        long otherMemoryCount = place.getMemories()
//                .stream()
//                .filter(m -> (!m.getUser().getUserSeq().equals(userSeq))  & user.getMemoryBlocks().stream().noneMatch(mb -> mb.getMemory().getId().equals(m.getId())) )
//                .count();


        return new PlaceResponseDto(
                place.getId(), // id
                place.getPosition(), // 좌표
                memoryCounts.get("myMemoryCount"), // 내가 쓴 메모리 개수
                memoryCounts.get("otherMemoryCount"), // 상대방이 쓴 메모리 개수
                place.getIsScraped(), // 해당 장소 스크랩 여부
                place.getPlaceCategory(), // 해당 장소 카테고리
                place.getPlaceName(), // 장소 이름
                GroupType.ALL, // 해당 장소 타입
                place.getPlaceImage() // 장소 이미지
        );


    }

    private boolean checkIsScraped(User user, Place place)
    {
        return user.getPlaceScraps().stream().anyMatch(s -> s.getPlace().getId().equals(place.getId()));
    }

}
