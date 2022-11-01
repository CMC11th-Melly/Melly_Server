package cmc.mellyserver.group.presentation.dto;

import cmc.mellyserver.group.domain.UserGroup;

public class GroupAssembler {

      public static GroupCreateResponse groupCreateResponse(UserGroup userGroup)
      {
          return new GroupCreateResponse(userGroup.getId(),userGroup.getGroupName(),userGroup.getGroupType(),userGroup.getGroupIcon());
      }
}
