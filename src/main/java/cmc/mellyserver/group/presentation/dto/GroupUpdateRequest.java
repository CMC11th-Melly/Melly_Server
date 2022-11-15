package cmc.mellyserver.group.presentation.dto;

import cmc.mellyserver.group.domain.enums.GroupType;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Data
@NoArgsConstructor
public class GroupUpdateRequest {

    @NotNull
    @Size(max = 23,message = "그룹명은 23자 이하입니다.")
    private String groupName;
    private GroupType groupType;
    private Integer groupIcon;
}
