package cmc.mellyserver.place.placeScrap.domain;

import cmc.mellyserver.common.exception.ExceptionCodeAndDetails;
import cmc.mellyserver.common.exception.GlobalBadRequestException;
import cmc.mellyserver.common.util.auth.AuthenticatedUserChecker;
import cmc.mellyserver.place.domain.Place;
import cmc.mellyserver.place.domain.PlaceQueryRepository;
import cmc.mellyserver.place.domain.PlaceRepository;
import cmc.mellyserver.place.domain.Position;
import cmc.mellyserver.common.enums.ScrapType;
import cmc.mellyserver.place.placeScrap.application.dto.PlaceScrapResponseDto;
import cmc.mellyserver.place.placeScrap.application.dto.ScrapedPlaceResponseDto;
import cmc.mellyserver.place.placeScrap.presentation.dto.ScrapAssembler;
import cmc.mellyserver.user.domain.User;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class PlaceScrapDomainService {


    private final PlaceRepository placeRepository;
    private final PlaceQueryRepository placeQueryRepository;
    private final PlaceScrapRepository scrapRepository;
    private final AuthenticatedUserChecker authenticatedUserChecker;


    /**
     * 유저가 스크랩한 장소 조회
     * @param scrapType 스크랩 타입으로 필터링
     */
    public Slice<ScrapedPlaceResponseDto> getScrapPlace(Pageable pageable, String uid, ScrapType scrapType)
    {
        User user = authenticatedUserChecker.checkAuthenticatedUserExist(uid);

        Slice<Place> result = placeQueryRepository.getScrapedPlace(pageable,user,scrapType);
        return result.map(p -> ScrapAssembler.scrapedPlaceResponseDto(user,p));
    }


    /**
     * 스크랩 타입 별 장소 개수 조회
     */
    public List<PlaceScrapResponseDto> getScrapedPlaceCount(String uid)
    {
        User user = authenticatedUserChecker.checkAuthenticatedUserExist(uid);
        return placeQueryRepository.getScrapedPlaceGrouping(user);
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
            scrapRepository.save(PlaceScrap.createScrap(user,savePlace,scrapType));
        }
        else
        {
            scrapRepository.save(PlaceScrap.createScrap(user,placeOpt.get(),scrapType));
        }

    }


    /**
     * 스크랩 제거
     */
    @Transactional
    public void removeScrap(String uid, Double lat, Double lng)
    {
        User user = authenticatedUserChecker.checkAuthenticatedUserExist(uid);

        checkExistScrap(user.getUserSeq(),lat,lng);
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
