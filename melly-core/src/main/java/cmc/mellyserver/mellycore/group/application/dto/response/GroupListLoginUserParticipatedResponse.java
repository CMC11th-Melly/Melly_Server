package cmc.mellyserver.mellycore.group.application.dto.response;

import cmc.mellyserver.mellycore.group.domain.repository.dto.GroupLoginUserParticipatedResponseDto;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class GroupListLoginUserParticipatedResponse {

    private List<GroupLoginUserParticipatedResponseDto> contents;

    private Boolean next;

    public static GroupListLoginUserParticipatedResponse from(List<GroupLoginUserParticipatedResponseDto> contents, Boolean next)
    {
        return new GroupListLoginUserParticipatedResponse(contents, next);
    }

}
