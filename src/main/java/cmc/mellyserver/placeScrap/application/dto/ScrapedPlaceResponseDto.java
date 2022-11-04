package cmc.mellyserver.placeScrap.application.dto;

import cmc.mellyserver.group.domain.enums.GroupType;
import cmc.mellyserver.place.application.dto.PlaceResponseDto;
import cmc.mellyserver.place.domain.Position;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
public class ScrapedPlaceResponseDto {

    private Long placeId = -1L;
    private Position position;
    private Long myMemoryCount;
    private Long otherMemoryCount;
    private Boolean isScraped;
    private String PlaceCategory = "";
    private String placeName = "";
    private GroupType recommendType = GroupType.ALL;
    private String placeImage;

    public static ScrapedPlaceResponseDto placeNotCreated(Position position, Long myMemoryCount, Long otherMemoryCount, Boolean isScraped)
    {
        return new ScrapedPlaceResponseDto(position,myMemoryCount,otherMemoryCount,isScraped);

    }

    private ScrapedPlaceResponseDto(Position position, Long myMemoryCount, Long otherMemoryCount, Boolean isScraped) {

        this.position = position;
        this.myMemoryCount = myMemoryCount;
        this.otherMemoryCount = otherMemoryCount;
        this.isScraped = isScraped;
    }
}
