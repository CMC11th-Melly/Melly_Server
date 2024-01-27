package cmc.mellyserver.domain.group;

import org.springframework.stereotype.Component;

import cmc.mellyserver.dbcore.group.UserGroup;
import cmc.mellyserver.support.exception.BusinessException;
import cmc.mellyserver.support.exception.ErrorCode;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class GroupValidator {

    private static final int GROUP_MEMBER_MAX_COUNT = 9;

    private final GroupAndUserReader groupAndUserReader;

    public void isDuplicatedJoin(Long userId, Long groupId) {
        if (groupAndUserReader.findByUserIdAndGroupId(userId, groupId).isPresent()) {
            throw new BusinessException(ErrorCode.DUPLICATED_GROUP);
        }
    }

    public void isMaximumGroupMember(Long groupId) {
        if (groupAndUserReader.countGroupMembers(groupId) > GROUP_MEMBER_MAX_COUNT) {
            throw new BusinessException(ErrorCode.PARTICIPATE_GROUP_NOT_POSSIBLE);
        }
    }

    public void checkRemoveAuthority(Long userId, UserGroup userGroup) {
        if (!userGroup.checkAuthority(userId)) {
            throw new BusinessException(ErrorCode.NO_AUTHORITY_TO_REMOVE);
        }
    }
}
