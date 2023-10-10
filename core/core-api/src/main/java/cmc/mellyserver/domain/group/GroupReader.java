package cmc.mellyserver.domain.group;

import cmc.mellyserver.dbcore.group.GroupRepository;
import cmc.mellyserver.dbcore.group.UserGroup;
import cmc.mellyserver.domain.group.query.UserGroupQueryRepository;
import cmc.mellyserver.domain.group.query.dto.GroupLoginUserParticipatedResponseDto;
import cmc.mellyserver.support.exception.BusinessException;
import cmc.mellyserver.support.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class GroupReader {

    private final GroupRepository groupRepository;

    private final UserGroupQueryRepository userGroupQueryRepository;

    public UserGroup findById(Long groupId) {
        return groupRepository.findById(groupId).orElseThrow(() -> new BusinessException(ErrorCode.NO_SUCH_GROUP));
    }

    public Slice<GroupLoginUserParticipatedResponseDto> groupListLoginUserParticipate(Long userId, Long groupId, Pageable pageable) {
        return userGroupQueryRepository.getGroupListLoginUserParticipate(userId, groupId, pageable);
    }
}
