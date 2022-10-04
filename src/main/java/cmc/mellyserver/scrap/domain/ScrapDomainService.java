package cmc.mellyserver.scrap.domain;

import cmc.mellyserver.common.exception.ExceptionCodeAndDetails;
import cmc.mellyserver.common.exception.GlobalBadRequestException;
import cmc.mellyserver.common.util.AuthenticatedUserChecker;
import cmc.mellyserver.group.domain.enums.GroupType;
import cmc.mellyserver.memory.domain.GroupInfo;
import cmc.mellyserver.memory.domain.Memory;
import cmc.mellyserver.memory.domain.enums.OpenType;
import cmc.mellyserver.place.domain.Place;
import cmc.mellyserver.place.domain.PlaceRepository;
import cmc.mellyserver.place.domain.Position;
import cmc.mellyserver.place.domain.enums.ScrapType;
import cmc.mellyserver.scrap.application.dto.ScrapedPlaceResponseDto;
import cmc.mellyserver.user.domain.User;
import cmc.mellyserver.user.domain.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ScrapDomainService {

    private final PlaceRepository placeRepository;
    private final UserRepository userRepository;
    private final ScrapRepository scrapRepository;
    private final AuthenticatedUserChecker authenticatedUserChecker;

    public List<ScrapedPlaceResponseDto> getScrapPlace(String uid)
    {
        User user = authenticatedUserChecker.checkAuthenticatedUserExist(uid);

        return user.getScraps().stream().map(s -> new ScrapedPlaceResponseDto(s.getPlace().getId(),
                s.getPlace().getName(),
                s.getPlace().getPlaceImage())).collect(Collectors.toList());
    }


    @Transactional
    public void createScrap(String uid, Double lat, Double lng, ScrapType scrapType)
    {

        User user = authenticatedUserChecker.checkAuthenticatedUserExist(uid);

        checkDuplicatedScrap(user.getUserSeq(),lat,lng);

        Optional<Place> placeOpt = placeRepository.findPlaceByPosition(new Position(lat,lng));

        if(placeOpt.isEmpty())
        {
            Place savePlace = placeRepository.save(Place.builder().position(new Position(lat, lng)).build());
            Scrap scrap = new Scrap(scrapType);
            savePlace.addScrap(user);
            scrapRepository.save(scrap);
        }
        else{
            Scrap scrap = new Scrap(scrapType);
            scrap.setPlace(placeOpt.get());
            scrap.setUser(user);
            scrapRepository.save(scrap);
        }

    }

    @Transactional
    public void removeScrap(String uid, Long placeId)
    {
        User user = authenticatedUserChecker.checkAuthenticatedUserExist(uid);

        checkExistScrap(placeId, user.getUserSeq());

        scrapRepository.deleteByUserUserSeqAndPlaceId(user.getUserSeq(),placeId);
    }



    private void checkDuplicatedScrap(Long userSeq, Double lat, Double lng) {
        scrapRepository.findByUserUserSeqAndPlacePosition(userSeq,new Position(lat,lng))
                .ifPresent(x -> {throw new GlobalBadRequestException(ExceptionCodeAndDetails.DUPLICATE_SCRAP);});
    }

    private void checkExistScrap(Long placeId, Long userSeq) {
        scrapRepository.findByPlaceIdAndUserUserSeq(placeId,userSeq)
                .orElseThrow(() -> {throw new GlobalBadRequestException(ExceptionCodeAndDetails.NOT_EXIST_SCRAP);});
    }

}
