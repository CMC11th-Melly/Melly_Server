package cmc.mellyserver.mellycore.scrap.domain.repository.dto;

import cmc.mellyserver.mellycore.place.domain.Position;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class ScrapedPlaceResponseDto {

    private Long placeId;

    private Position position;

    private String placeCategory;

    private String placeName;

    private String placeImage;

}
