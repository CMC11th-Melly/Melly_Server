package cmc.mellyserver.mellycore.common.fixture;

import cmc.mellyserver.mellycore.group.domain.UserGroup;
import cmc.mellyserver.mellycore.group.domain.enums.GroupType;

public class GroupFixtures {

    private static final String 그룹_이름 = "학교 동기들";

    private static final GroupType 그룹_타입 = GroupType.FRIEND;

    public static UserGroup 친구그룹() {

        return UserGroup.builder()
                .groupName(그룹_이름)
                .groupType(그룹_타입)
                .groupIcon(1)
                .build();
    }
}
