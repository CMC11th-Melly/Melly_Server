package cmc.mellyserver.mellycore.group.domain.repository.dto;

import cmc.mellyserver.mellycore.group.domain.enums.GroupType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class GroupListForSaveMemoryResponseDto {

    private Long groupId;
    private String groupName;
    private GroupType groupType;
}
