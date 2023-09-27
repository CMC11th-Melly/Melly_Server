package cmc.mellyserver.mellyapi.controller.group.dto.response;

import cmc.mellyserver.dbcore.group.enums.GroupType;
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
