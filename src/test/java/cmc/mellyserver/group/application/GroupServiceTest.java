package cmc.mellyserver.group.application;

import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class GroupServiceTest {


//    @InjectMocks
//    GroupService groupService;
//
//    @Mock
//    GroupRepository groupRepository;
//
//    @Mock
//    AuthenticatedUserChecker authenticatedUserChecker;
//
//    @Test
//    @DisplayName("로그인한 유저가 그룹을 만든다.")
//    void create_group()
//    {
//        BDDMockito.doReturn(UserFactory.createEmailLoginUser())
//                .when(authenticatedUserChecker)
//                .checkAuthenticatedUserExist(BDDMockito.any());
//
//        BDDMockito.doReturn(new UserGroup("떡잎마을방범대","http",GroupType.FRIEND))
//                .when(groupRepository).save(BDDMockito.any(UserGroup.class));
//
//        UserGroup userGroup = groupService.saveGroup(
//                "uuid",
//                GroupCreateRequest.builder().
//                        groupName("떡잎마을방범대").
//                        groupType(GroupType.FRIEND).build());
//
//        BDDMockito.verify(groupRepository, Mockito.times(1)).save(BDDMockito.any(UserGroup.class));
//        BDDMockito.verify(authenticatedUserChecker,Mockito.times(1)).checkAuthenticatedUserExist(BDDMockito.any());
//
//
//    }

}