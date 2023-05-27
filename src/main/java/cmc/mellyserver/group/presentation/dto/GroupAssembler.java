package cmc.mellyserver.group.presentation.dto;

import cmc.mellyserver.group.domain.UserGroup;
import cmc.mellyserver.user.application.dto.response.GroupLoginUserParticipatedResponseDto;


public class GroupAssembler {

      public static GroupLoginUserParticipatedResponseDto getUserGroupResponse(UserGroup userGroup)
      {
          return new GroupLoginUserParticipatedResponseDto(userGroup.getId(),
                  userGroup.getGroupIcon(),
                  userGroup.getGroupName(),
                  userGroup.getGroupType(),
                  userGroup.getInviteLink()
          );
      }

}
