package cmc.mellyserver.place.application.dto;

import cmc.mellyserver.group.domain.enums.GroupType;
import cmc.mellyserver.place.domain.Position;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.security.core.parameters.P;

@Data
@AllArgsConstructor
public class PlaceResponseDto {

    private Long placeId = -1L;
    private Position position;
    private Long myMemoryCount;
    private Long otherMemoryCount;
    private Boolean isScraped;
    private String PlaceCategory = "";
    private String placeName = "";
    private GroupType recommendType = GroupType.ALL;
    private String placeImage;

    public static PlaceResponseDto PlaceNotCreated(Position position,Long myMemoryCount,Long otherMemoryCount,Boolean isScraped)
    {
        return new PlaceResponseDto(position,myMemoryCount,otherMemoryCount,isScraped);

    }

    private PlaceResponseDto(Position position, Long myMemoryCount, Long otherMemoryCount, Boolean isScraped) {

        this.position = position;
        this.myMemoryCount = myMemoryCount;
        this.otherMemoryCount = otherMemoryCount;
        this.isScraped = isScraped;
    }
}
