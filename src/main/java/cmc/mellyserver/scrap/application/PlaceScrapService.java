package cmc.mellyserver.scrap.application;

import cmc.mellyserver.common.enums.ScrapType;
import cmc.mellyserver.scrap.application.dto.PlaceScrapResponseDto;
import cmc.mellyserver.scrap.application.dto.ScrapedPlaceResponseDto;
import cmc.mellyserver.scrap.domain.PlaceScrapDomainService;
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
public class PlaceScrapService {

    private final PlaceScrapDomainService placeScrapDomainService;


    /**
     * 각 스크랩 타입별 장소 페이지 조회
     * TODO : 다시 안봐
     */
    public Slice<ScrapedPlaceResponseDto> getScrapedPlace(Pageable pageable, Long userSeq, ScrapType scrapType)
    {
        return placeScrapDomainService.getScrapPlace(pageable, userSeq,scrapType);
    }


    /**
     * 각 스크랩 타입별 장소 개수 조회 로직
     * TODO : 다시 안봐
     */
    public List<PlaceScrapResponseDto> getScrapedPlaceCount(Long userSeq)
    {
        return placeScrapDomainService.getScrapedPlaceCount(userSeq);
    }


    /**
     * 스크랩 생성
     */
    @Transactional
    public void createScrap(Long userSeq, ScrapRequest scrapRequest)
    {
        placeScrapDomainService.createScrap(userSeq, scrapRequest.getLat(), scrapRequest.getLng(), scrapRequest.getScrapType(),scrapRequest.getPlaceName(),scrapRequest.getPlaceCategory());
    }


    /**
     * 스크랩 삭제
     */
    @Transactional
    public void removeScrap(Long userSeq,Double lat, Double lng)
    {
        placeScrapDomainService.removeScrap(userSeq,lat,lng);
    }

}
