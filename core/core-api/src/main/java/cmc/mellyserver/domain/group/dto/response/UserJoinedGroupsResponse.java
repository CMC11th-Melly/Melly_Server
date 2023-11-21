package cmc.mellyserver.domain.group.dto.response;

import java.util.List;

import cmc.mellyserver.domain.group.query.dto.UserJoinedGroupsResponseDto;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class UserJoinedGroupsResponse {

    private List<UserJoinedGroupsResponseDto> contents;

    private boolean next;

    public static UserJoinedGroupsResponse from(List<UserJoinedGroupsResponseDto> contents,
        boolean next) {
        return new UserJoinedGroupsResponse(contents, next);
    }

}
