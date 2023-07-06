package cmc.mellyserver.mellycore.group.application.dto.request;

import cmc.mellyserver.mellycommon.enums.GroupType;
import cmc.mellyserver.mellycore.group.domain.UserGroup;
import lombok.Builder;
import lombok.Data;

@Data
public class CreateGroupRequestDto {

    private Long userSeq;

    private String groupName;

    private GroupType groupType;

    private int groupIcon;

    @Builder
    public CreateGroupRequestDto(Long userSeq, String groupName, GroupType groupType,
                                 int groupIcon) {
        this.userSeq = userSeq;
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
