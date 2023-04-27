package cmc.mellyserver.user.presentation.dto.common;

import cmc.mellyserver.user.presentation.dto.response.GetUserGroupResponseDto;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class GetUserGroupResponseWrapper {

    List<GetUserGroupResponseDto> groupInfo;
}
