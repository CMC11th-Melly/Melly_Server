package cmc.mellyserver.user.presentation.dto.response;

import cmc.mellyserver.group.domain.enums.GroupType;
import cmc.mellyserver.user.presentation.dto.common.UserDto;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class GetUserGroupResponse {

    private Long groupId;
    @Schema(example = "1부터 10까지 숫자")
    private int groupIcon;
    private String groupName;

    private List<UserDto> users;
    private GroupType groupType;
    private String invitationLink;

}
