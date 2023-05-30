package cmc.mellyserver.place.application.dto;

import cmc.mellyserver.common.enums.GroupType;
import cmc.mellyserver.place.domain.Position;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@AllArgsConstructor
@Builder
public class MarkedPlaceResponseDto {

    private Position position;
    private GroupType groupType;
    private Long placeId;
    private Long memoryCount;

}
