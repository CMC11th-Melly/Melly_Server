package cmc.mellyserver.group.application.dto.request;

import cmc.mellyserver.common.enums.GroupType;
import cmc.mellyserver.group.presentation.dto.request.GroupCreateRequest;
import lombok.Builder;
import lombok.Data;

@Data
public class CreateGroupRequestDto {

    private Long userSeq;

    private String groupName;

    private GroupType groupType;

    private int groupIcon;

    @Builder
    public CreateGroupRequestDto(Long userSeq, String groupName, GroupType groupType, int groupIcon) {
        this.userSeq = userSeq;
        this.groupName = groupName;
        this.groupType = groupType;
        this.groupIcon = groupIcon;
    }

    public static CreateGroupRequestDto of(Long userSeq, GroupCreateRequest groupCreateRequest)
    {
        return CreateGroupRequestDto.builder()
                .userSeq(userSeq)
                .groupName(groupCreateRequest.getGroupName())
                .groupType(groupCreateRequest.getGroupType())
                .groupIcon(groupCreateRequest.getGroupIcon())
                .build();
    }
}
