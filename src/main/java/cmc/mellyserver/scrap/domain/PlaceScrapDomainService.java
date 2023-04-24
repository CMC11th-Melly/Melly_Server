package cmc.mellyserver.scrap.domain;

import cmc.mellyserver.common.exception.ExceptionCodeAndDetails;
import cmc.mellyserver.common.exception.GlobalBadRequestException;
import cmc.mellyserver.common.util.auth.AuthenticatedUserChecker;
import cmc.mellyserver.place.domain.Place;
import cmc.mellyserver.place.domain.repository.PlaceQueryRepository;
import cmc.mellyserver.place.domain.repository.PlaceRepository;
import cmc.mellyserver.place.domain.Position;
import cmc.mellyserver.common.enums.ScrapType;
import cmc.mellyserver.scrap.application.dto.PlaceScrapResponseDto;
import cmc.mellyserver.scrap.application.dto.ScrapedPlaceResponseDto;
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
     * TODO : 다시 안봐
     */
    public Slice<ScrapedPlaceResponseDto> getScrapPlace(Pageable pageable, Long userSeq, ScrapType scrapType)
    {
       return placeQueryRepository.getScrapedPlace(pageable,userSeq,scrapType);
    }


    /**
     * 스크랩 타입 별 장소 개수 조회
     * TODO : 다시 안봐
     */
    public List<PlaceScrapResponseDto> getScrapedPlaceCount(Long userSeq)
    {
        User user = authenticatedUserChecker.checkAuthenticatedUserExist(userSeq);
        return placeQueryRepository.getScrapedPlaceGrouping(user);
    }

    @Transactional
    public void createScrap(Long userSeq, Double lat, Double lng, ScrapType scrapType,String placeName,String placeCategory)
    {
        checkDuplicatedScrap(userSeq,lat,lng);
        Optional<Place> placeOpt = placeRepository.findPlaceByPosition(new Position(lat,lng));

        if(placeOpt.isEmpty())
        {
            Place savePlace = placeRepository.save(Place.builder().placeName(placeName).placeCategory(placeCategory).position(new Position(lat, lng)).build());
            scrapRepository.save(PlaceScrap.createScrap(userSeq,savePlace.getId(),scrapType));
        }
        else
        {
            scrapRepository.save(PlaceScrap.createScrap(userSeq,placeOpt.get().getId(),scrapType));
        }
    }


    @Transactional
    public void removeScrap(Long userSeq, Double lat, Double lng)
    {
        checkExistScrap(userSeq,lat,lng);
        scrapRepository.deleteByUserUserSeqAndPlacePosition(userSeq,new Position(lat,lng));
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
