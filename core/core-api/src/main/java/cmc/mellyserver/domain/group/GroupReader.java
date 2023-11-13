package cmc.mellyserver.domain.group;

import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Component;

import cmc.mellyserver.dbcore.group.GroupRepository;
import cmc.mellyserver.dbcore.group.UserGroup;
import cmc.mellyserver.domain.group.dto.response.GroupListLoginUserParticipatedResponse;
import cmc.mellyserver.domain.group.query.UserGroupQueryRepository;
import cmc.mellyserver.domain.group.query.dto.GroupLoginUserParticipatedResponseDto;
import cmc.mellyserver.support.exception.BusinessException;
import cmc.mellyserver.support.exception.ErrorCode;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class GroupReader {

  private final GroupRepository groupRepository;

  private final UserGroupQueryRepository userGroupQueryRepository;

  public UserGroup findById(Long groupId) {
	return groupRepository.findById(groupId).orElseThrow(() -> new BusinessException(ErrorCode.NO_SUCH_GROUP));
  }

  public GroupListLoginUserParticipatedResponse groupListLoginUserParticipate(Long userId, Long lastId,
	  Pageable pageable) {
	return transferToList(userGroupQueryRepository.getGroupListLoginUserParticipate(userId, lastId, pageable));
  }

  private GroupListLoginUserParticipatedResponse transferToList(
	  Slice<GroupLoginUserParticipatedResponseDto> groupLoginUserParticipatedResponseDtos) {
	List<GroupLoginUserParticipatedResponseDto> contents = groupLoginUserParticipatedResponseDtos.getContent();
	boolean next = groupLoginUserParticipatedResponseDtos.hasNext();
	return GroupListLoginUserParticipatedResponse.from(contents, next);
  }
}
