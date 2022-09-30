package cmc.mellyserver.place.domain;

import cmc.mellyserver.group.domain.GroupType;
import cmc.mellyserver.memory.domain.OpenType;
import lombok.AccessLevel;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class PlaceInfoRequest {

    private Double lat;
    private Double lng;
    private String title;
    private String content;
    private String keyword;
    private Long groupId;
    private int star;
    private GroupType groupType;
    private OpenType  openType;
}
