package cmc.mellyserver.user.presentation.dto;

import cmc.mellyserver.group.domain.enums.GroupType;
import cmc.mellyserver.memory.domain.enums.OpenType;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
@AllArgsConstructor
public class GetUserGroupResponse {

    private Long groupId;
    private String groupIcon;
    private String groupName;
    private GroupType groupType;
    private String invitationLink;
    @JsonFormat(shape= JsonFormat.Shape.STRING, pattern="yyyyMMdd")
    private LocalDateTime createdDate;
    private List<UserResponseDto> userInfo;

}
