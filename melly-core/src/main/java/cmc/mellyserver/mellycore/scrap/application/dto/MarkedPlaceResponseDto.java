package cmc.mellyserver.mellycore.scrap.application.dto;

import cmc.mellyserver.mellycommon.enums.GroupType;
import cmc.mellyserver.mellycore.place.domain.Position;
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
