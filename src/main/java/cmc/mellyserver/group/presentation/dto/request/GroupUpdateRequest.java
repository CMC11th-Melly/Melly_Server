package cmc.mellyserver.group.presentation.dto.request;

import cmc.mellyserver.common.enums.GroupType;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Size;

@Data
@NoArgsConstructor
public class GroupUpdateRequest {
    @Size(max = 23,message = "그룹명은 23자 이하입니다.")
    private String groupName;
    private GroupType groupType;
    private Integer groupIcon;
}
