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
    private Long myMemoryCount;
    private Long otherMemoryCount;
    private Boolean isScraped;
    private String placeName;
    private GroupType recommendType;
    private String placeImage;
    private String placeCategory;
    @JsonFormat(shape= JsonFormat.Shape.STRING, pattern="yyyyMMddHHmm")
    private LocalDateTime scrapedDate;

}
