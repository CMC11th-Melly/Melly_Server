package cmc.mellyserver.group.presentation.dto;

import cmc.mellyserver.group.domain.enums.GroupType;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class GroupUpdateRequest {

    private String groupName;
    private GroupType groupType;
    private Integer groupIcon;
}
