package cmc.mellyserver.mellycore.memory.domain.vo;

import javax.persistence.Embeddable;

import cmc.mellyserver.mellycommon.enums.GroupType;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@Embeddable
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class GroupInfo {
	private String groupName;
	private GroupType groupType;
	private Long groupId;
}
