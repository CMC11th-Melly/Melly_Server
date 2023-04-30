package cmc.mellyserver.memory.domain;

import cmc.mellyserver.common.enums.GroupType;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Embeddable;

@Getter
@AllArgsConstructor
@Embeddable
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class GroupInfo {
    private String groupName;
    private GroupType groupType;
    private Long groupId;
}
