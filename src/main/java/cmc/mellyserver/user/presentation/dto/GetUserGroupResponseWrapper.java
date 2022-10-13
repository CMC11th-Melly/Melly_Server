package cmc.mellyserver.user.presentation.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class GetUserGroupResponseWrapper {

    List<GetUserGroupResponse> groupInfo;
}
