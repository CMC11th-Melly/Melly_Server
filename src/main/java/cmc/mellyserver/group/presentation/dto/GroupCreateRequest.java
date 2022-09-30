package cmc.mellyserver.group.presentation.dto;

import cmc.mellyserver.group.domain.GroupType;
import lombok.AccessLevel;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Data
public class GroupCreateRequest {

    private String groupName;
    private GroupType groupType;

}
