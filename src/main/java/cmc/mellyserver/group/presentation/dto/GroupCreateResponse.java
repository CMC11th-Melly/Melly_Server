package cmc.mellyserver.group.presentation.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class GroupCreateResponse {
    private Long groupId;
    private String groupName;
}
