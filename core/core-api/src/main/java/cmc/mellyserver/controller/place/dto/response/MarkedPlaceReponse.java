package cmc.mellyserver.controller.place.dto.response;


import cmc.mellyserver.dbcore.group.enums.GroupType;
import cmc.mellyserver.dbcore.place.Position;
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
