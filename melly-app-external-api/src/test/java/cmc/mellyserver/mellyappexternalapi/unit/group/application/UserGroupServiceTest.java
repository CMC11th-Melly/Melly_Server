package cmc.mellyserver.mellyappexternalapi.unit.group.application;

import cmc.mellyserver.mellyappexternalapi.common.auth.AuthenticatedUserChecker;
import cmc.mellyserver.mellyappexternalapi.common.exception.ExceptionCodeAndDetails;
import cmc.mellyserver.mellyappexternalapi.common.exception.GlobalBadRequestException;
import cmc.mellyserver.mellyappexternalapi.common.factory.UserGroupFactory;
import cmc.mellyserver.mellyappexternalapi.group.application.dto.request.CreateGroupRequestDto;
import cmc.mellyserver.mellyappexternalapi.group.application.dto.request.UpdateGroupRequestDto;
import cmc.mellyserver.mellyappexternalapi.group.application.impl.GroupServiceImpl;
import cmc.mellyserver.mellydomain.common.enums.DeleteStatus;
import cmc.mellyserver.mellydomain.common.enums.GroupType;
import cmc.mellyserver.mellydomain.group.domain.UserGroup;
import cmc.mellyserver.mellydomain.group.domain.repository.GroupAndUserRepository;
import cmc.mellyserver.mellydomain.group.domain.repository.GroupRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatCode;
import static org.mockito.BDDMockito.*;

@ExtendWith(MockitoExtension.class)
public class UserGroupServiceTest {

    @InjectMocks
    private GroupServiceImpl groupService;

    @Mock
    private GroupRepository groupRepository;

    @Mock
    private AuthenticatedUserChecker authenticatedUserChecker;

    @Mock
    private GroupAndUserRepository groupAndUserRepository;

    @DisplayName("그룹을 추가 가능하다.")
    @Test
    void create_group() {
        // given
        UserGroup group = UserGroupFactory.mockUserGroup();
        CreateGroupRequestDto createGroupRequestDto = UserGroupFactory.mockCreateGroupRequestDto();
        given(groupRepository.save(any(UserGroup.class))).willReturn(group);

        // when
        UserGroup group1 = groupService.saveGroup(createGroupRequestDto);

        // then
        assertThat(group1.getCreatorId()).isEqualTo(createGroupRequestDto.getUserSeq());
    }

    @DisplayName("그룹을 식별자를 통해 그룹 정보를 조회하려고 할때")
    @Nested
    class When_get_group_info_by_identifier {

        @DisplayName("그룹의 식별자를 통해 특정 그룹의 정보를 조회할 수 있다.")
        @Test
        void get_group_info() {
            // given
            UserGroup group = UserGroupFactory.mockUserGroup();
            given(groupRepository.findById(anyLong())).willReturn(Optional.of(group));

            // when
            UserGroup userGroup = groupService.findGroupById(1L);

            // then
            assertThat(userGroup.getId()).isEqualTo(group.getId());
            verify(groupRepository, times(1)).findById(anyLong());
        }

        @DisplayName("특정 그룹이 DB에 존재하지 않으면 예외가 발생한다.")
        @Test
        void get_group_info_not_found() {
            // given
            given(groupRepository.findById(anyLong())).willReturn(Optional.empty());

            // when
            assertThatCode(() -> groupService.findGroupById(1L))
                    .isInstanceOf(GlobalBadRequestException.class)
                    .hasMessage(ExceptionCodeAndDetails.NO_SUCH_GROUP.getMessage());
        }
    }

//    @DisplayName("사용자가 그룹에 참여하고자 할때")
//    @Nested
//    class When_participate_group {
//
//        @DisplayName("그룹이 존재하지 않으면 예외가 발생한다.")
//        @Test
//        void participate_group_not_exist_group() {
//            // given
//            given(authenticatedUserChecker.checkAuthenticatedUserExist(anyLong())).willReturn(
//                    UserFactory.createEmailLoginUser());
//            given(groupRepository.findById(anyLong())).willReturn(Optional.empty());
//
//            // then
//            assertThatCode(() -> groupService.participateToGroup(1L, 1L))
//                    .isInstanceOf(GlobalBadRequestException.class)
//                    .hasMessage(ExceptionCodeAndDetails.NO_SUCH_GROUP.getMessage());
//
//            verify(authenticatedUserChecker, times(1)).checkAuthenticatedUserExist(anyLong());
//            verify(groupRepository, times(1)).findById(anyLong());
//        }
//
//        @DisplayName("이미 그룹에 가입한 적이 있다면 예외가 발생한다.")
//        @Test
//        void participate_group_duplicated_group_participate() {
//            // given
//            given(authenticatedUserChecker.checkAuthenticatedUserExist(anyLong())).willReturn(
//                    UserFactory.createEmailLoginUser());
//            given(groupRepository.findById(anyLong())).willReturn(Optional.of(UserGroupFactory.mockUserGroup()));
//            given(groupAndUserRepository.findGroupAndUserByUserAndGroup(any(User.class),
//                    any(UserGroup.class))).willReturn(Optional.of(GroupAndUserFactory.mockGroupAndUser()));
//            // then
//            assertThatCode(() -> groupService.participateToGroup(1L, 1L))
//                    .isInstanceOf(GlobalBadRequestException.class)
//                    .hasMessage(ExceptionCodeAndDetails.DUPLICATED_GROUP.getMessage());
//
//            verify(authenticatedUserChecker, times(1)).checkAuthenticatedUserExist(anyLong());
//            verify(groupRepository, times(1)).findById(anyLong());
//            verify(groupAndUserRepository, times(1)).findGroupAndUserByUserAndGroup(any(User.class),
//                    any(UserGroup.class));
//        }
//
//        @DisplayName("그룹이 존재하고 기존에 가입한 적이 없다면 성공적으로 그룹에 등록된다.")
//        @Test
//        void participate_group() {
//            // given
//            given(authenticatedUserChecker.checkAuthenticatedUserExist(anyLong())).willReturn(
//                    UserFactory.createEmailLoginUser());
//            given(groupRepository.findById(anyLong())).willReturn(Optional.of(UserGroupFactory.mockUserGroup()));
//            given(groupAndUserRepository.findGroupAndUserByUserAndGroup(any(User.class),
//                    any(UserGroup.class))).willReturn(Optional.empty());
//            given(groupAndUserRepository.save(any(GroupAndUser.class))).willReturn(
//                    GroupAndUserFactory.mockGroupAndUser());
//
//            // when
//            groupService.participateToGroup(1L, 1L);
//
//            // then
//            verify(authenticatedUserChecker, times(1)).checkAuthenticatedUserExist(anyLong());
//            verify(groupRepository, times(1)).findById(anyLong());
//            verify(groupAndUserRepository, times(1)).findGroupAndUserByUserAndGroup(any(User.class),
//                    any(UserGroup.class));
//            verify(groupAndUserRepository, times(1)).save(any(GroupAndUser.class));
//        }
//    }

    @DisplayName("그룹을 수정하려고 할때")
    @Nested
    class When_update_group {

        @DisplayName("그룹이 존재하지 않으면 예외가 발생한다.")
        @Test
        void update_group_not_exist_exception() {
            // given
            UpdateGroupRequestDto updateGroupRequestDto = UserGroupFactory.mockUpdateGroupRequestDto();
            given(groupRepository.findById(anyLong())).willReturn(Optional.empty());

            // then
            assertThatCode(() -> groupService.updateGroup(1L, updateGroupRequestDto))
                    .isInstanceOf(GlobalBadRequestException.class)
                    .hasMessage(ExceptionCodeAndDetails.NO_SUCH_GROUP.getMessage());

            verify(groupRepository, times(1)).findById(anyLong());
        }

        @DisplayName("그룹이 존재하면 수정 가능하다.")
        @Test
        void update_group() {
            // given
            UserGroup group = new UserGroup(1L, "동기", 1, "http://naver.com", 1L, GroupType.FRIEND, DeleteStatus.N);
            UpdateGroupRequestDto updateGroupRequestDto = UserGroupFactory.mockUpdateGroupRequestDto();
            given(groupRepository.findById(anyLong())).willReturn(Optional.of(group));

            // when
            groupService.updateGroup(1L, updateGroupRequestDto);

            // then
            assertThat(group.getGroupName()).isEqualTo(updateGroupRequestDto.getGroupName());
            verify(groupRepository, times(1)).findById(anyLong());
        }
    }

    @DisplayName("그룹을 삭제하려고 할때")
    @Nested
    class When_remove_group {

        @DisplayName("그룹이 존재하지 않으면 예외 발생")
        @Test
        void remove_group_not_exist_group_exception() {
            // given
            given(groupRepository.findById(anyLong())).willReturn(Optional.empty());

            // then
            assertThatCode(() -> groupService.removeGroup(1L, 1L))
                    .isInstanceOf(GlobalBadRequestException.class)
                    .hasMessage(ExceptionCodeAndDetails.NO_SUCH_GROUP.getMessage());

            verify(groupRepository, times(1)).findById(anyLong());
        }

        @DisplayName("그룹 삭제 권한이 존재하지 않으면 삭제 불가")
        @Test
        void remove_group_not_permission_to_remove_group() {
            // given
            UserGroup group = new UserGroup(1L, "동기", 1, "http://naver.com", 2L, GroupType.FRIEND, DeleteStatus.N);
            given(groupRepository.findById(anyLong())).willReturn(Optional.of(group));

            // then
            assertThatCode(() -> groupService.removeGroup(1L, group.getId()))
                    .isInstanceOf(IllegalArgumentException.class);

            verify(groupRepository, times(1)).findById(anyLong());
        }

        @DisplayName("그룹 삭제 성공")
        @Test
        void remove_group() {

            // given
            UserGroup group = new UserGroup(1L, "동기", 1, "http://naver.com", 1L, GroupType.FRIEND, DeleteStatus.N);
            given(groupRepository.findById(anyLong())).willReturn(Optional.of(group));

            // when
            groupService.removeGroup(1L, 1L);

            // then
            verify(groupRepository, times(1)).findById(anyLong());
        }
    }

}
