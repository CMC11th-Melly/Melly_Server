package cmc.mellyserver.mellyappexternalapi.integration.group;

import cmc.mellyserver.mellyappexternalapi.common.exception.ExceptionCodeAndDetails;
import cmc.mellyserver.mellyappexternalapi.common.exception.GlobalBadRequestException;
import cmc.mellyserver.mellyappexternalapi.common.factory.UserFactory;
import cmc.mellyserver.mellyappexternalapi.common.factory.UserGroupFactory;
import cmc.mellyserver.mellyappexternalapi.group.application.dto.request.CreateGroupRequestDto;
import cmc.mellyserver.mellyappexternalapi.integration.IntegrationTest;
import cmc.mellyserver.mellydomain.common.enums.DeleteStatus;
import cmc.mellyserver.mellydomain.common.enums.GroupType;
import cmc.mellyserver.mellydomain.group.domain.UserGroup;
import cmc.mellyserver.mellydomain.user.domain.User;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatCode;

public class UserGroupServiceIntegrationTest extends IntegrationTest {

    @DisplayName("그룹을 추가할 수 있다.")
    @Test
    void create_group() {
        // given
        User savedUser = userRepository.save(UserFactory.createEmailLoginUser());
        CreateGroupRequestDto createGroupRequestDto = CreateGroupRequestDto.builder()
                .userSeq(savedUser.getUserSeq())
                .groupType(GroupType.FRIEND)
                .groupName("테스트 그룹")
                .groupIcon(1)
                .build();

        // when
        UserGroup group = groupService.saveGroup(createGroupRequestDto);

        // then
        assertThat(group.getGroupName()).isEqualTo(createGroupRequestDto.getGroupName());
        assertThat(group.getCreatorId()).isEqualTo(savedUser.getUserSeq());
        assertThat(group.getGroupIcon()).isEqualTo(createGroupRequestDto.getGroupIcon());
        assertThat(group.getGroupType()).isEqualTo(createGroupRequestDto.getGroupType());
    }

    @DisplayName("그룹 삭제 성공")
    @Test
    void remove_group() {
        // given
        User savedUser = userRepository.save(UserFactory.createEmailLoginUser());
        UserGroup savedGroup = groupService.saveGroup(CreateGroupRequestDto.builder()
                .userSeq(savedUser.getUserSeq())
                .groupIcon(1)
                .groupName("테스트 그룹")
                .groupType(GroupType.FRIEND)
                .build());

        // when
        groupService.removeGroup(savedUser.getUserSeq(), savedGroup.getId());

        // then
        UserGroup removeGroup = groupService.findGroupById(savedGroup.getId());
        assertThat(removeGroup.getIsDeleted()).isEqualTo(DeleteStatus.Y);
    }

    @DisplayName("그룹을 식별자를 통해 그룹 정보를 조회하려고 할때")
    @Nested
    class When_get_group_info_by_identifier {

        @DisplayName("그룹의 식별자를 통해 특정 그룹의 정보를 조회할 수 있다.")
        @Test
        void get_group_info() {
            // given
            UserGroup savedGroup = groupRepository.save(UserGroupFactory.userGroup());

            // when
            UserGroup group = groupService.findGroupById(savedGroup.getId());

            // then
            assertThat(group.getGroupName()).isEqualTo(savedGroup.getGroupName());
            assertThat(group.getGroupIcon()).isEqualTo(savedGroup.getGroupIcon());
            assertThat(group.getGroupType()).isEqualTo(savedGroup.getGroupType());
        }

        @DisplayName("특정 그룹이 DB에 존재하지 않으면 예외가 발생한다.")
        @Test
        void get_group_info_not_found() {
            // given
            groupRepository.save(UserGroupFactory.mockUserGroup());

            // when
            assertThatCode(() -> groupService.findGroupById(2L))
                    .isInstanceOf(GlobalBadRequestException.class)
                    .hasMessage(ExceptionCodeAndDetails.NO_SUCH_GROUP.getMessage());
        }
    }

}
