package fixtures;

import cmc.mellyserver.dbcore.group.GroupType;
import cmc.mellyserver.dbcore.group.UserGroup;

public abstract class GroupFixtures {

    private static final String 그룹_이름 = "학교 동기들";

    private static final GroupType 그룹_타입 = GroupType.FRIEND;

    private static final String 가족_이름 = "가족";

    private static final GroupType 가족_타입 = GroupType.FAMILY;

    public static UserGroup 친구그룹(Long ownerId) {

        return UserGroup.builder().name(그룹_이름).groupType(그룹_타입).icon(1).ownerId(ownerId).build();
    }

    public static UserGroup 가족그룹(Long ownerId) {
        return UserGroup.builder().name(가족_이름).groupType(가족_타입).icon(1).ownerId(ownerId).build();
    }
}
