package cmc.mellyserver.mellycore.group.application.dto.request;

import cmc.mellyserver.mellycore.group.domain.UserGroup;
import cmc.mellyserver.mellycore.group.domain.enums.GroupType;
import lombok.Builder;
import lombok.Data;

@Data
public class CreateGroupRequestDto {

    private Long creatorId;

    private String groupName;

    private GroupType groupType;

    private int groupIcon;

    @Builder
    public CreateGroupRequestDto(Long id, String groupName, GroupType groupType,
                                 int groupIcon) {
        this.creatorId = id;
        this.groupName = groupName;
        this.groupType = groupType;
        this.groupIcon = groupIcon;
    }

    public UserGroup toEntity() {
        return UserGroup.builder()
                .groupName(groupName)
                .groupIcon(groupIcon)
                .groupType(groupType)
                .build();
    }

}
