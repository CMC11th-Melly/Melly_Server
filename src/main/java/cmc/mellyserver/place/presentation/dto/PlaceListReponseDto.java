package cmc.mellyserver.place.presentation.dto;

import cmc.mellyserver.group.domain.enums.GroupType;
import cmc.mellyserver.place.domain.Position;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class PlaceListReponseDto {

    private Position position;
    private GroupType groupType;
    private Long placeId;
    private Long memoryCount;
}
