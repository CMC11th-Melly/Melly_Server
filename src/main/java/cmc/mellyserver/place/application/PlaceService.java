package cmc.mellyserver.place.application;

import cmc.mellyserver.common.exception.ExceptionCodeAndDetails;
import cmc.mellyserver.common.exception.GlobalBadRequestException;
import cmc.mellyserver.group.domain.enums.GroupType;
import cmc.mellyserver.place.domain.Place;
import cmc.mellyserver.place.domain.PlaceQueryRepository;
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

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class PlaceService {

    private final PlaceQueryRepository placeQueryRepository;
    private final PlaceDomainService placeDomainService;
    private final UserRepository userRepository;


    public GetPlaceInfoDto getPlaceInfo(Long placeId, String uid)
    {
        return placeDomainService.getPlaceInfo(placeId, uid);
    }


    public List<PlaceListReponseDto> getPlaceList(String userId, GroupType groupType)
    {
        User user = userRepository.findUserByUserId(userId).orElseThrow(() -> {
            throw new GlobalBadRequestException(ExceptionCodeAndDetails.NO_SUCH_USER);
        });
        List<Place> placeUserMemoryExist = placeQueryRepository.getPlaceUserMemoryExist(user);
        return PlaceAssembler.placeListReponseDto(placeUserMemoryExist, groupType, userId);
    }

}
