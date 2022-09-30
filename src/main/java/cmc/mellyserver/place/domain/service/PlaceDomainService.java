package cmc.mellyserver.place.domain.service;

import cmc.mellyserver.common.exception.ExceptionCodeAndDetails;
import cmc.mellyserver.common.exception.GlobalBadRequestException;
import cmc.mellyserver.memory.domain.enums.OpenType;
import cmc.mellyserver.place.domain.Place;
import cmc.mellyserver.place.domain.PlaceRepository;
import cmc.mellyserver.place.domain.service.dto.GetPlaceInfoDto;
import cmc.mellyserver.place.domain.service.dto.MemoryImageDto;
import cmc.mellyserver.place.domain.service.dto.MyMemoryDto;
import cmc.mellyserver.place.domain.service.dto.OtherMemoryDto;
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
}
