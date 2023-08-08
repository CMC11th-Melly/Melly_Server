package cmc.mellyserver.mellycore.integration.group;


import cmc.mellyserver.mellycore.integration.IntegrationTest;

public class UserGroupServiceIntegrationTest extends IntegrationTest {

//    @DisplayName("그룹을 추가할 수 있다.")
//    @Test
//    void create_group() {
//        // given
//        User savedUser = userRepository.save(UserFactory.createEmailLoginUser());
//        CreateGroupRequestDto createGroupRequestDto = CreateGroupRequestDto.cmc.mellyserver.mellycore.builder()
//                .id(savedUser.getId())
//                .groupType(GroupType.FRIEND)
//                .groupName("테스트 그룹")
//                .groupIcon(1)
//                .build();
//
//        // when
//        UserGroup group = groupService.saveGroup(createGroupRequestDto);
//
//        // then
//        assertThat(group.getGroupName()).isEqualTo(createGroupRequestDto.getGroupName());
//        assertThat(group.getCreatorId()).isEqualTo(savedUser.getId());
//        assertThat(group.getGroupIcon()).isEqualTo(createGroupRequestDto.getGroupIcon());
//        assertThat(group.getGroupType()).isEqualTo(createGroupRequestDto.getGroupType());
//    }
//
//    @DisplayName("그룹 삭제 성공")
//    @Test
//    void remove_group() {
//        // given
//        User savedUser = userRepository.save(UserFactory.createEmailLoginUser());
//        UserGroup savedGroup = groupService.saveGroup(CreateGroupRequestDto.cmc.mellyserver.mellycore.builder()
//                .id(savedUser.getId())
//                .groupIcon(1)
//                .groupName("테스트 그룹")
//                .groupType(GroupType.FRIEND)
//                .build());
//
//        // when
//        groupService.removeGroup(savedUser.getId(), savedGroup.getId());
//
//        // then
//        UserGroup removeGroup = groupService.findGroupById(savedGroup.getId());
//        assertThat(removeGroup.getIsDeleted()).isEqualTo(DeleteStatus.Y);
//    }
//
//    @DisplayName("그룹을 식별자를 통해 그룹 정보를 조회하려고 할때")
//    @Nested
//    class When_get_group_info_by_identifier {
//
//        @DisplayName("그룹의 식별자를 통해 특정 그룹의 정보를 조회할 수 있다.")
//        @Test
//        void get_group_info() {
//            // given
//            UserGroup savedGroup = groupRepository.save(UserGroupFactory.userGroup());
//
//            // when
//            UserGroup group = groupService.findGroupById(savedGroup.getId());
//
//            // then
//            assertThat(group.getGroupName()).isEqualTo(savedGroup.getGroupName());
//            assertThat(group.getGroupIcon()).isEqualTo(savedGroup.getGroupIcon());
//            assertThat(group.getGroupType()).isEqualTo(savedGroup.getGroupType());
//        }
//
//        @DisplayName("특정 그룹이 DB에 존재하지 않으면 예외가 발생한다.")
//        @Test
//        void get_group_info_not_found() {
//            // given
//            groupRepository.save(UserGroupFactory.mockUserGroup());
//
//            // when
//            assertThatCode(() -> groupService.findGroupById(2L))
//                    .isInstanceOf(GlobalBadRequestException.class)
//                    .hasMessage(ErrorCode.NO_SUCH_GROUP.getMessage());
//        }
//    }

}
