package cmc.mellyserver.controller.group.dto.response;

import cmc.mellyserver.dbcore.group.GroupType;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class GroupCreateResponse {

  private Long groupId;

  private String groupName;

  private GroupType groupType;

  private int groupIcon;

}
