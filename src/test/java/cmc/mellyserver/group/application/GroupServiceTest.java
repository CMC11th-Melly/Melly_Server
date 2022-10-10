package cmc.mellyserver.group.application;

import cmc.mellyserver.common.factory.UserFactory;
import cmc.mellyserver.common.util.auth.AuthenticatedUserChecker;
import cmc.mellyserver.group.domain.GroupRepository;
import cmc.mellyserver.group.domain.UserGroup;
import cmc.mellyserver.group.domain.enums.GroupType;
import cmc.mellyserver.group.presentation.dto.GroupCreateRequest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.BDDMockito;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class GroupServiceTest {


    @InjectMocks
    GroupService groupService;

    @Mock
    GroupRepository groupRepository;

    @Mock
    AuthenticatedUserChecker authenticatedUserChecker;

    @Test
    @DisplayName("로그인한 유저가 그룹을 만든다.")
    void create_group()
    {
        BDDMockito.doReturn(UserFactory.createEmailLoginUser())
                .when(authenticatedUserChecker)
                .checkAuthenticatedUserExist(BDDMockito.any());

        BDDMockito.doReturn(new UserGroup("떡잎마을방범대","http",GroupType.FRIEND))
                .when(groupRepository).save(BDDMockito.any(UserGroup.class));

        UserGroup userGroup = groupService.saveGroup(
                "uuid",
                GroupCreateRequest.builder().
                        groupName("떡잎마을방범대").
                        groupType(GroupType.FRIEND).build());

        BDDMockito.verify(groupRepository, Mockito.times(1)).save(BDDMockito.any(UserGroup.class));
        BDDMockito.verify(authenticatedUserChecker,Mockito.times(1)).checkAuthenticatedUserExist(BDDMockito.any());


    }

}