package cmc.mellyserver.place.presentation.dto;

import cmc.mellyserver.group.domain.GroupType;
import lombok.AccessLevel;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class PlaceGroupCond {
    private GroupType groupType;
}
