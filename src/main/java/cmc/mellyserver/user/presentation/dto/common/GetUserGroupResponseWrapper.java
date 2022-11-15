package cmc.mellyserver.user.presentation.dto.common;

import cmc.mellyserver.user.presentation.dto.response.GetUserGroupResponse;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class GetUserGroupResponseWrapper {

    List<GetUserGroupResponse> groupInfo;
}
