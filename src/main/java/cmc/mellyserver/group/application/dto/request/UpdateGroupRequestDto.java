package cmc.mellyserver.group.application.dto.request;

import cmc.mellyserver.common.enums.GroupType;
import cmc.mellyserver.group.presentation.dto.request.GroupUpdateRequest;
import lombok.Builder;
import lombok.Data;

@Data
public class UpdateGroupRequestDto {

    private Long groupId;

    private String groupName;

    private GroupType groupType;

    private int groupIcon;

    @Builder
    public UpdateGroupRequestDto(Long groupId, String groupName, GroupType groupType, int groupIcon) {
        this.groupId = groupId;
        this.groupName = groupName;
        this.groupType = groupType;
        this.groupIcon = groupIcon;
    }

    public static UpdateGroupRequestDto of(Long groupId, GroupUpdateRequest groupUpdateRequest)
    {
        return UpdateGroupRequestDto.builder()
                .groupId(groupId)
                .groupName(groupUpdateRequest.getGroupName())
                .groupType(groupUpdateRequest.getGroupType())
                .groupIcon(groupUpdateRequest.getGroupIcon())
                .build();
    }
}
