package cmc.mellyserver.mellyapi.domain.group.dto.request;


import cmc.mellyserver.dbcore.group.UserGroup;
import cmc.mellyserver.dbcore.group.enums.GroupType;
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
