package cmc.mellyserver.mellyappexternalapi.group.presentation.dto.request;

import cmc.mellyserver.mellydomain.common.enums.GroupType;
import javax.validation.constraints.Size;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class GroupUpdateRequest {

    @Size(max = 23, message = "그룹명은 23자 이하입니다.")
    private String groupName;
    private GroupType groupType;
    private Integer groupIcon;

    @Builder
    public GroupUpdateRequest(String groupName, GroupType groupType, Integer groupIcon) {
        this.groupName = groupName;
        this.groupType = groupType;
        this.groupIcon = groupIcon;
    }
}
