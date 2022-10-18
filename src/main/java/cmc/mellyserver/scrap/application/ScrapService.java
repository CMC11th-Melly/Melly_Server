package cmc.mellyserver.scrap.application;

import cmc.mellyserver.scrap.application.dto.ScrapedPlaceResponseDto;
import cmc.mellyserver.scrap.domain.Scrap;
import cmc.mellyserver.scrap.domain.ScrapDomainService;
import cmc.mellyserver.scrap.domain.ScrapRepository;
import cmc.mellyserver.scrap.presentation.dto.ScrapRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class ScrapService {

    private final ScrapDomainService scrapDomainService;

    @Transactional
    public void createScrap(String uid, ScrapRequest scrapRequest)
    {
        scrapDomainService.createScrap(uid, scrapRequest.getLat(), scrapRequest.getLng(),
                                            scrapRequest.getScrapType(),scrapRequest.getPlaceName(),scrapRequest.getPlaceCategory());
    }

    public Slice<ScrapedPlaceResponseDto> getScrapedPlace(Long lastId, Pageable pageable, String uid)
    {
        return scrapDomainService.getScrapPlace(lastId, pageable, uid);
    }

    @Transactional
    public void removeScrap(String uid,Double lat, Double lng)
    {
        scrapDomainService.removeScrap(uid,lat,lng);
    }

}
