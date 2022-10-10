package cmc.mellyserver.group.presentation.dto;

import cmc.mellyserver.group.domain.enums.GroupType;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Data
public class GroupCreateRequest {

    private String groupName;
    private GroupType groupType;

    @Builder
    public GroupCreateRequest(String groupName, GroupType groupType) {
        this.groupName = groupName;
        this.groupType = groupType;
    }
}
