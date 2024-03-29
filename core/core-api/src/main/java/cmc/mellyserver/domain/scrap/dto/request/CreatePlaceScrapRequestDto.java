package cmc.mellyserver.domain.scrap.dto.request;

import cmc.mellyserver.dbcore.place.Place;
import cmc.mellyserver.dbcore.place.Position;
import cmc.mellyserver.dbcore.scrap.ScrapType;
import lombok.Builder;
import lombok.Data;

@Data
public class CreatePlaceScrapRequestDto {

    private Position position;

    private ScrapType scrapType;

    private String placeName;

    private String placeCategory;

    @Builder
    public CreatePlaceScrapRequestDto(Position position, ScrapType scrapType, String placeName,
        String placeCategory) {
        this.position = position;
        this.scrapType = scrapType;
        this.placeName = placeName;
        this.placeCategory = placeCategory;
    }

    public Place toEntity() {
        return Place.builder()
            .name(placeName)
            .category(placeCategory)
            .position(position)
            .build();
    }

}
