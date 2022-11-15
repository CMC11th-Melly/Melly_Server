package cmc.mellyserver.group.presentation.dto;

import cmc.mellyserver.group.domain.UserGroup;
import cmc.mellyserver.user.presentation.dto.response.GetUserGroupResponse;

import java.util.Collections;


public class GroupAssembler {

      public static GetUserGroupResponse getUserGroupResponse(UserGroup userGroup)
      {
          return new GetUserGroupResponse(userGroup.getId(),
                  userGroup.getGroupIcon(),
                  userGroup.getGroupName(),
                  Collections.emptyList(),
                  userGroup.getGroupType(),
                  userGroup.getInviteLink()
          );
      }

}
