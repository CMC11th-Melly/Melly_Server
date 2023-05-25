package cmc.mellyserver.scrap.application.dto.request;

import cmc.mellyserver.common.enums.ScrapType;
import cmc.mellyserver.scrap.presentation.dto.request.ScrapRequest;
import lombok.Builder;
import lombok.Data;

@Data
public class CreatePlaceScrapRequestDto {

    private Long userSeq;

    private Double lat;

    private Double lng;

    private ScrapType scrapType;

    private String placeName;

    private String placeCategory;

    @Builder
    public CreatePlaceScrapRequestDto(Long userSeq, Double lat, Double lng, ScrapType scrapType, String placeName, String placeCategory) {
        this.userSeq = userSeq;
        this.lat = lat;
        this.lng = lng;
        this.scrapType = scrapType;
        this.placeName = placeName;
        this.placeCategory = placeCategory;
    }

    public static CreatePlaceScrapRequestDto of(Long userSeq, ScrapRequest scrapRequest)
    {
        return CreatePlaceScrapRequestDto.builder()
                .userSeq(userSeq)
                .lat(scrapRequest.getLat())
                .lng(scrapRequest.getLng())
                .scrapType(scrapRequest.getScrapType())
                .placeName(scrapRequest.getPlaceName())
                .placeCategory(scrapRequest.getPlaceCategory())
                .build();
    }
}
