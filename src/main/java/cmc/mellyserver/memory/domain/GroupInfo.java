package cmc.mellyserver.memory.domain;

import cmc.mellyserver.group.domain.GroupType;
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
    private GroupType groupType;
    private Long groupId;
}
