package cmc.mellyserver.place.presentation.dto.response;

import cmc.mellyserver.common.enums.GroupType;
import cmc.mellyserver.place.domain.Position;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
public class MarkedPlaceReponse {

    private Position position;
    private GroupType groupType;
    private Long placeId;
    private Long memoryCount;

    @Builder
    public MarkedPlaceReponse(Position position, GroupType groupType, Long placeId, Long memoryCount) {
        this.position = position;
        this.groupType = groupType;
        this.placeId = placeId;
        this.memoryCount = memoryCount;
    }
}
