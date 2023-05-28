package cmc.mellyserver.group.presentation.dto;

import cmc.mellyserver.group.domain.UserGroup;
import cmc.mellyserver.user.application.dto.response.GroupLoginUserParticipatedResponseDto;
import cmc.mellyserver.user.presentation.dto.response.GroupLoginUserParticipatedResponse;


public abstract class GroupAssembler {


    public static GroupLoginUserParticipatedResponse getUserGroupResponse(UserGroup userGroup)
    {
        return new GroupLoginUserParticipatedResponse(userGroup.getId(),
                userGroup.getGroupIcon(),
                userGroup.getGroupName(),
                userGroup.getGroupType(),
                userGroup.getInviteLink()
        );
    }

}
