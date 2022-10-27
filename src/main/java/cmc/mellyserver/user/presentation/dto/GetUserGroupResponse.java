package cmc.mellyserver.user.presentation.dto;

import cmc.mellyserver.group.domain.enums.GroupType;
import cmc.mellyserver.memory.domain.enums.OpenType;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDateTime;
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
//    @JsonFormat(shape= JsonFormat.Shape.STRING, pattern="yyyyMMdd")
//    private LocalDateTime createdDate;


}
