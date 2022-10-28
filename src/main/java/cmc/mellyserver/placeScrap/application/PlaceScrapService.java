package cmc.mellyserver.placeScrap.application;

import cmc.mellyserver.place.domain.Place;
import cmc.mellyserver.placeScrap.application.dto.PlaceScrapResponseDto;
import cmc.mellyserver.placeScrap.application.dto.ScrapedPlaceResponseDto;
import cmc.mellyserver.placeScrap.domain.PlaceScrapDomainService;
import cmc.mellyserver.placeScrap.presentation.dto.ScrapRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class PlaceScrapService {

    private final PlaceScrapDomainService placeScrapDomainService;

    @Transactional
    public void createScrap(String uid, ScrapRequest scrapRequest)
    {
        placeScrapDomainService.createScrap(uid, scrapRequest.getLat(), scrapRequest.getLng(),
                                            scrapRequest.getScrapType(),scrapRequest.getPlaceName(),scrapRequest.getPlaceCategory());
    }

    public Slice<ScrapedPlaceResponseDto> getScrapedPlace(Pageable pageable, String uid)
    {
        return placeScrapDomainService.getScrapPlace(pageable, uid);
    }

    public List<PlaceScrapResponseDto> getScrapedPlaceGroup(Pageable pageable, String uid)
    {
      return   placeScrapDomainService.getScrapPlaceGroup(pageable, uid);

    }

    @Transactional
    public void removeScrap(String uid,Double lat, Double lng)
    {
        placeScrapDomainService.removeScrap(uid,lat,lng);
    }

}
