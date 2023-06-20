package cmc.mellyserver.mellyappexternalapi.group.application.dto.request;

import cmc.mellyserver.mellydomain.common.enums.GroupType;
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

}
