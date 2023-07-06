package cmc.mellyserver.mellycore.scrap.application.dto.request;

import cmc.mellyserver.mellycommon.enums.ScrapType;
import cmc.mellyserver.mellycore.place.domain.Place;
import cmc.mellyserver.mellycore.place.domain.Position;
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
    public CreatePlaceScrapRequestDto(Long userSeq, Double lat, Double lng, ScrapType scrapType, String placeName,
                                      String placeCategory) {
        this.userSeq = userSeq;
        this.lat = lat;
        this.lng = lng;
        this.scrapType = scrapType;
        this.placeName = placeName;
        this.placeCategory = placeCategory;
    }

    public Place toEntity() {
        return Place.builder().placeName(placeName).placeCategory(placeCategory).position(new Position(lat, lng)).build();
    }
}
