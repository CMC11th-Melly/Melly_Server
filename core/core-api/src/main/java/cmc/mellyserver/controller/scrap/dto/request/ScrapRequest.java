package cmc.mellyserver.controller.scrap.dto.request;

import cmc.mellyserver.dbcore.scrap.ScrapType;
import cmc.mellyserver.domain.scrap.dto.request.CreatePlaceScrapRequestDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ScrapRequest {

    private Double lat;

    private Double lng;

    private ScrapType scrapType;

    private String placeName;

    private String placeCategory;

    public CreatePlaceScrapRequestDto toServiceRequest() {
        return CreatePlaceScrapRequestDto.builder()
            .lat(lat)
            .lng(lng)
            .scrapType(scrapType)
            .placeName(placeName)
            .placeCategory(placeCategory)
            .build();
    }

}
