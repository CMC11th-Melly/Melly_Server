package cmc.mellyserver.place.placeScrap.application;

import cmc.mellyserver.common.enums.ScrapType;
import cmc.mellyserver.place.placeScrap.application.dto.PlaceScrapResponseDto;
import cmc.mellyserver.place.placeScrap.application.dto.ScrapedPlaceResponseDto;
import cmc.mellyserver.place.placeScrap.domain.PlaceScrapDomainService;
import cmc.mellyserver.place.placeScrap.presentation.dto.ScrapRequest;
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


    /**
     * 각 스크랩 타입별 장소 페이지 조회
     */
    public Slice<ScrapedPlaceResponseDto> getScrapedPlace(Pageable pageable, String uid, ScrapType scrapType)
    {
        return placeScrapDomainService.getScrapPlace(pageable, uid,scrapType);
    }


    /**
     * 각 스크랩 타입별 장소 개수 조회 로직
     */
    public List<PlaceScrapResponseDto> getScrapedPlaceCount(String uid)
    {
        return placeScrapDomainService.getScrapedPlaceCount(uid);
    }


    /**
     * 스크랩 생성
     */
    @Transactional
    public void createScrap(String uid, ScrapRequest scrapRequest)
    {
        placeScrapDomainService.createScrap(uid, scrapRequest.getLat(), scrapRequest.getLng(), scrapRequest.getScrapType(),scrapRequest.getPlaceName(),scrapRequest.getPlaceCategory());
    }


    /**
     * 스크랩 삭제
     */
    @Transactional
    public void removeScrap(String uid,Double lat, Double lng)
    {
        placeScrapDomainService.removeScrap(uid,lat,lng);
    }

}
