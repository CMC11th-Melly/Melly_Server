package cmc.mellyserver.domain.group;

import org.springframework.stereotype.Component;

import cmc.mellyserver.common.aop.lock.annotation.DistributedLock;
import cmc.mellyserver.support.exception.BusinessException;
import cmc.mellyserver.support.exception.ErrorCode;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class GroupValidator {

  private static final int GROUP_MEMBER_MAX_COUNT = 9;
  private static final int GROUP_MEMBER_EXIT_LIMIT = 2;

  private final GroupAndUserReader groupAndUserReader;

  public void isDuplicatedJoin(final Long userId, final Long groupId) {
	if (groupAndUserReader.findByUserIdAndGroupId(userId, groupId).isPresent()) {
	  throw new BusinessException(ErrorCode.DUPLICATED_GROUP);
	}
  }

  @DistributedLock(key = "#groupId")
  public void isMaximumGroupMember(Long groupId) {
	if (groupAndUserReader.countGroupMembers(groupId) > GROUP_MEMBER_MAX_COUNT) {
	  throw new BusinessException(ErrorCode.PARTICIPATE_GROUP_NOT_POSSIBLE);
	}
  }

  @DistributedLock(key = "#groupId")
  public boolean isGroupRemovable(Long groupId) {
	return groupAndUserReader.countGroupMembers(groupId) < GROUP_MEMBER_EXIT_LIMIT;
  }
}
