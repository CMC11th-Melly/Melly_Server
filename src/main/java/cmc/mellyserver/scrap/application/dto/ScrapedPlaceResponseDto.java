package cmc.mellyserver.scrap.application.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ScrapedPlaceResponseDto {
    private Long placeId;
    private String placeName;
    private String placeImage;
}
