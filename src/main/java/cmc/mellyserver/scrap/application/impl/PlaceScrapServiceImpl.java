package cmc.mellyserver.scrap.application.impl;

import cmc.mellyserver.common.enums.ScrapType;
import cmc.mellyserver.common.exception.ExceptionCodeAndDetails;
import cmc.mellyserver.common.exception.GlobalBadRequestException;
import cmc.mellyserver.common.util.auth.AuthenticatedUserChecker;
import cmc.mellyserver.place.domain.Place;
import cmc.mellyserver.place.domain.Position;
import cmc.mellyserver.place.domain.repository.PlaceQueryRepository;
import cmc.mellyserver.place.domain.repository.PlaceRepository;
import cmc.mellyserver.scrap.application.PlaceScrapService;
import cmc.mellyserver.scrap.application.dto.request.CreatePlaceScrapRequestDto;
import cmc.mellyserver.scrap.application.dto.response.PlaceScrapCountResponseDto;
import cmc.mellyserver.scrap.application.dto.response.ScrapedPlaceResponseDto;
import cmc.mellyserver.scrap.domain.PlaceScrap;
import cmc.mellyserver.scrap.domain.repository.PlaceScrapRepository;
import cmc.mellyserver.scrap.presentation.dto.request.ScrapRequest;
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
public class PlaceScrapServiceImpl implements PlaceScrapService {

    private final PlaceRepository placeRepository;

    private final PlaceQueryRepository placeQueryRepository;

    private final PlaceScrapRepository scrapRepository;

    private final AuthenticatedUserChecker authenticatedUserChecker;


    @Override
    public Slice<ScrapedPlaceResponseDto> findScrapedPlace(Pageable pageable, Long userSeq, ScrapType scrapType)
    {
        return placeQueryRepository.getScrapedPlace(pageable,userSeq,scrapType);
    }


    @Override
    public List<PlaceScrapCountResponseDto> countByPlaceScrapType(Long userSeq)
    {
        User user = authenticatedUserChecker.checkAuthenticatedUserExist(userSeq);
        return placeQueryRepository.getScrapedPlaceGrouping(user);
    }


    @Override
    @Transactional
    public void createScrap(CreatePlaceScrapRequestDto createPlaceScrapRequestDto)
    {
        Optional<Place> placeOpt = placeRepository.findPlaceByPosition(new Position(createPlaceScrapRequestDto.getLat(), createPlaceScrapRequestDto.getLng()));
        User user = authenticatedUserChecker.checkAuthenticatedUserExist(createPlaceScrapRequestDto.getUserSeq());
        if(placeOpt.isEmpty())
        {
            Place savePlace = placeRepository.save(Place.builder().placeName(createPlaceScrapRequestDto.getPlaceName()).placeCategory(createPlaceScrapRequestDto.getPlaceCategory()).position(new Position(createPlaceScrapRequestDto.getLat(), createPlaceScrapRequestDto.getLng())).build());
            scrapRepository.save(PlaceScrap.createScrap(user,savePlace,createPlaceScrapRequestDto.getScrapType()));
        }
        else
        {
            checkDuplicatedScrap(createPlaceScrapRequestDto.getUserSeq(),placeOpt.get().getId());
            scrapRepository.save(PlaceScrap.createScrap(user,placeOpt.get(),createPlaceScrapRequestDto.getScrapType()));
        }
    }


    @Override
    @Transactional
    public void removeScrap(Long userSeq,Double lat, Double lng)
    {
        Place place = placeRepository.findPlaceByPosition(new Position(lat, lng)).orElseThrow(() -> {
            throw new GlobalBadRequestException(ExceptionCodeAndDetails.NO_SUCH_PLACE);
        });
        checkExistScrap(userSeq,place.getId());
        scrapRepository.deleteByUserUserSeqAndPlacePosition(userSeq,new Position(lat,lng));
    }



    private void checkDuplicatedScrap(Long userSeq, Long placeId) {
        scrapRepository.findByUserUserSeqAndPlaceId(userSeq,placeId)
                .ifPresent(x -> {throw new GlobalBadRequestException(ExceptionCodeAndDetails.DUPLICATE_SCRAP);});
    }



    private void checkExistScrap(Long userSeq, Long placeId) {
        scrapRepository.findByUserUserSeqAndPlaceId(userSeq, placeId)
                .orElseThrow(() -> {throw new GlobalBadRequestException(ExceptionCodeAndDetails.NOT_EXIST_SCRAP);});
    }
}
