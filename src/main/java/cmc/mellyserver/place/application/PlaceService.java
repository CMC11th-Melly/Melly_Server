package cmc.mellyserver.place.application;

import cmc.mellyserver.common.exception.ExceptionCodeAndDetails;
import cmc.mellyserver.common.exception.GlobalBadRequestException;
import cmc.mellyserver.common.util.AuthenticatedUserChecker;
import cmc.mellyserver.group.domain.enums.GroupType;
import cmc.mellyserver.memory.domain.enums.OpenType;
import cmc.mellyserver.place.application.dto.PlaceResponseDto;
import cmc.mellyserver.place.domain.Place;
import cmc.mellyserver.place.domain.PlaceQueryRepository;
import cmc.mellyserver.place.domain.service.dto.GetPlaceInfo;
import cmc.mellyserver.place.domain.service.dto.GetPlaceInfoDto;
import cmc.mellyserver.place.domain.service.PlaceDomainService;
import cmc.mellyserver.place.presentation.dto.PlaceAssembler;
import cmc.mellyserver.place.presentation.dto.PlaceListReponseDto;
import cmc.mellyserver.user.domain.User;
import cmc.mellyserver.user.domain.UserRepository;
import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class PlaceService {

    private final PlaceQueryRepository placeQueryRepository;
    private final PlaceDomainService placeDomainService;
    private final AuthenticatedUserChecker authenticatedUserChecker;




    public PlaceResponseDto getPlaceByPosition(String uid, Double lat, Double lng)
    {
        return placeDomainService.getPlace(uid, lat, lng);
    }




    public List<PlaceListReponseDto> getPlaceList(String uid, GroupType groupType)
    {
        User user = authenticatedUserChecker.checkAuthenticatedUserExist(uid);
        List<Place> placeUserMemoryExist = placeQueryRepository.getPlaceUserMemoryExist(user);
        return PlaceAssembler.placeListReponseDto(placeUserMemoryExist, groupType, uid);
    }

    public List<PlaceResponseDto> placeSearchByMemory(String uid,Long placeId) {
        User user = authenticatedUserChecker.checkAuthenticatedUserExist(uid);
        List<Place> placeByMemory = placeQueryRepository.getPlaceByMemory(placeId);
        return placeByMemory.stream().map(p -> new PlaceResponseDto(p.getId(),p.getPosition(),
                p.getMemories().
                        stream().
                        filter(m -> m.getUser().getUserId().equals(user.getUserId())).count(),
                p.getMemories().stream().filter(m -> (!m.getUser().getUserId().equals(user.getUserId())) & m.getOpenType().equals(OpenType.ALL)).count(),
                p.getIsScraped(),
                p.getPlaceCategory(),
                p.getName(),
                null,
                p.getPlaceImage()
        )).collect(Collectors.toList());

    }
}
