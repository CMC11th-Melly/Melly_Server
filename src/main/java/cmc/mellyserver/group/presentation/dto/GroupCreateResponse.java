package cmc.mellyserver.group.presentation.dto;

import cmc.mellyserver.group.domain.enums.GroupType;
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
