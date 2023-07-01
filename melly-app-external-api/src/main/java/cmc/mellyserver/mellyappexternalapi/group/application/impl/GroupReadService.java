package cmc.mellyserver.mellyappexternalapi.group.application.impl;

import cmc.mellyserver.mellyappexternalapi.common.exception.ExceptionCodeAndDetails;
import cmc.mellyserver.mellyappexternalapi.common.exception.GlobalBadRequestException;
import cmc.mellyserver.mellydomain.group.domain.UserGroup;
import cmc.mellyserver.mellydomain.group.domain.repository.GroupRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Transactional(readOnly = true)
@Service
@RequiredArgsConstructor
public class GroupReadService {

    private final GroupRepository groupRepository;

    public UserGroup findGroupById(Long groupId) {

        return groupRepository.findById(groupId).orElseThrow(() -> {
            throw new GlobalBadRequestException(ExceptionCodeAndDetails.NO_SUCH_GROUP);
        });
    }
}
