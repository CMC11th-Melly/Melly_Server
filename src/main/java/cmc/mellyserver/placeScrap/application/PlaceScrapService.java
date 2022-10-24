package cmc.mellyserver.placeScrap.application;

import cmc.mellyserver.placeScrap.application.dto.ScrapedPlaceResponseDto;
import cmc.mellyserver.placeScrap.domain.PlaceScrapDomainService;
import cmc.mellyserver.placeScrap.presentation.dto.ScrapRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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

    public Slice<ScrapedPlaceResponseDto> getScrapedPlace(Long lastId, Pageable pageable, String uid)
    {
        return placeScrapDomainService.getScrapPlace(lastId, pageable, uid);
    }

    @Transactional
    public void removeScrap(String uid,Double lat, Double lng)
    {
        placeScrapDomainService.removeScrap(uid,lat,lng);
    }

}
