package cmc.mellyserver.user.presentation.dto.response;

import cmc.mellyserver.common.enums.GroupType;
import cmc.mellyserver.user.presentation.dto.common.UserDto;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
public class GetUserGroupResponseDto {

    private Long groupId;
    @Schema(example = "1부터 10까지 숫자")
    private int groupIcon;
    private String groupName;
    private List<UserDto> users;
    private GroupType groupType;
    private String invitationLink;

    public GetUserGroupResponseDto(Long groupId, int groupIcon, String groupName, GroupType groupType, String invitationLink) {
        this.groupId = groupId;
        this.groupIcon = groupIcon;
        this.groupName = groupName;
        this.groupType = groupType;
        this.invitationLink = invitationLink;
    }
}
