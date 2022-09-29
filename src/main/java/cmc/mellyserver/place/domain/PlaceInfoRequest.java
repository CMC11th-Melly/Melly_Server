package cmc.mellyserver.place.domain;

import cmc.mellyserver.group.domain.GroupType;
import lombok.AccessLevel;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class PlaceInfoRequest {
    private Double lat;
    private Double lng;
    private String title;
    private GroupType groupType;
}
