package cmc.mellyserver.mellydomain.group.domain.repository.dto;

import cmc.mellyserver.mellydomain.common.enums.GroupType;
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