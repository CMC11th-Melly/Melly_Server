package cmc.mellyserver.placeScrap.application.dto;

import cmc.mellyserver.group.domain.enums.GroupType;
import cmc.mellyserver.place.domain.Position;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
public class ScrapedPlaceResponseDto {
    private Long placeId;
    private Position position;
    private Boolean isScraped;
    private String placeName;
    private String placeImage;
    private String placeCategory;
}
