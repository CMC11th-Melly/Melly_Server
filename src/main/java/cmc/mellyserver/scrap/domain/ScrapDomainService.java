package cmc.mellyserver.scrap.domain;

import cmc.mellyserver.common.exception.ExceptionCodeAndDetails;
import cmc.mellyserver.common.exception.GlobalBadRequestException;
import cmc.mellyserver.common.util.auth.AuthenticatedUserChecker;
import cmc.mellyserver.place.domain.Place;
import cmc.mellyserver.place.domain.PlaceQueryRepository;
import cmc.mellyserver.place.domain.PlaceRepository;
import cmc.mellyserver.place.domain.Position;
import cmc.mellyserver.place.domain.enums.ScrapType;
import cmc.mellyserver.place.presentation.dto.PlaceAssembler;
import cmc.mellyserver.scrap.application.dto.ScrapedPlaceResponseDto;
import cmc.mellyserver.scrap.presentation.dto.ScrapAssembler;
import cmc.mellyserver.user.domain.User;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
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
    private final PlaceQueryRepository placeQueryRepository;
    private final ScrapRepository scrapRepository;
    private final AuthenticatedUserChecker authenticatedUserChecker;


    public Slice<ScrapedPlaceResponseDto> getScrapPlace(Long lastId, Pageable pageable, String uid)
    {
        User user = authenticatedUserChecker.checkAuthenticatedUserExist(uid);

        Slice<Place> result = placeQueryRepository.getScrapedPlace(lastId, pageable, user);
        return result.map(p -> ScrapAssembler.scrapedPlaceResponseDto(p,user,p.getCreatedDate()));
    }


    @Transactional
    public void createScrap(String uid, Double lat, Double lng, ScrapType scrapType,String placeName,String placeCategory)
    {

        User user = authenticatedUserChecker.checkAuthenticatedUserExist(uid);

        checkDuplicatedScrap(user.getUserSeq(),lat,lng);

        Optional<Place> placeOpt = placeRepository.findPlaceByPosition(new Position(lat,lng));

        if(placeOpt.isEmpty())
        {
            Place savePlace = placeRepository.save(Place.builder().placeName(placeName).placeCategory(placeCategory).position(new Position(lat, lng)).build());
            scrapRepository.save(Scrap.createScrap(user,savePlace,scrapType));
        }
        else{
            scrapRepository.save(Scrap.createScrap(user,placeOpt.get(),scrapType));
        }

    }


    @Transactional
    public void removeScrap(String uid, Double lat, Double lng)
    {
        User user = authenticatedUserChecker.checkAuthenticatedUserExist(uid);

        checkExistScrap(user.getUserSeq(),lat,lng );

        scrapRepository.deleteByUserUserSeqAndPlacePosition(user.getUserSeq(),new Position(lat,lng));
    }


    private void checkDuplicatedScrap(Long userSeq, Double lat, Double lng) {
        scrapRepository.findByUserUserSeqAndPlacePosition(userSeq,new Position(lat,lng))
                .ifPresent(x -> {throw new GlobalBadRequestException(ExceptionCodeAndDetails.DUPLICATE_SCRAP);});
    }


    private void checkExistScrap(Long userSeq, Double lat, Double lng) {
        scrapRepository.findByUserUserSeqAndPlacePosition(userSeq,new Position(lat,lng))
                .orElseThrow(() -> {throw new GlobalBadRequestException(ExceptionCodeAndDetails.NOT_EXIST_SCRAP);});
    }

}
