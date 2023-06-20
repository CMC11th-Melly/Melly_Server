package cmc.mellyserver.mellyappexternalapi.group.presentation.dto.response;

import cmc.mellyserver.mellydomain.common.enums.GroupType;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class GroupCreateResponse {

    private Long groupId;
    private String groupName;
    private GroupType groupType;
    private int groupIcon;
}
