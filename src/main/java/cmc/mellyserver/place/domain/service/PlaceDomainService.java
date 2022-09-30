package cmc.mellyserver.place.domain.service;

import cmc.mellyserver.common.exception.ExceptionCodeAndDetails;
import cmc.mellyserver.common.exception.GlobalBadRequestException;
import cmc.mellyserver.memory.domain.MemoryRepository;
import cmc.mellyserver.memory.domain.OpenType;
import cmc.mellyserver.place.domain.Place;
import cmc.mellyserver.place.domain.PlaceQueryRepository;
import cmc.mellyserver.place.domain.PlaceRepository;
import cmc.mellyserver.user.domain.User;
import cmc.mellyserver.user.domain.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PlaceDomainService {

    private final PlaceRepository placeRepository;
    private final UserRepository userRepository;

    /*
    1. Place 데이터 불러오기
    2. Place와 연관된 메모리 불러오기
    3. 사용자와 관련된 메모리와 다른 사람들과 관련된 메모리 분리해서 렌더링
     */
    public GetPlaceInfoDto getPlaceInfo(Long placeId, String userId)
    {
        // 인증유저가 있는지 다시한번 체크
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
        //m.getOpenType().equals(OpenType.ALL)
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
}
