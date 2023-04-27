package cmc.mellyserver.group.presentation.dto;

import cmc.mellyserver.group.domain.UserGroup;
import cmc.mellyserver.user.presentation.dto.response.GetUserGroupResponseDto;


public class GroupAssembler {

      public static GetUserGroupResponseDto getUserGroupResponse(UserGroup userGroup)
      {
          return new GetUserGroupResponseDto(userGroup.getId(),
                  userGroup.getGroupIcon(),
                  userGroup.getGroupName(),
                  userGroup.getGroupType(),
                  userGroup.getInviteLink()
          );
      }

}
