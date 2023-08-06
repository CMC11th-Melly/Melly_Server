package cmc.mellyserver.mellycore.integration.memory;

import cmc.mellyserver.mellyapi.common.factory.UserFactory;
import cmc.mellyserver.mellycommon.codes.ErrorCode;
import cmc.mellyserver.mellycommon.enums.AgeGroup;
import cmc.mellyserver.mellycommon.enums.GroupType;
import cmc.mellyserver.mellycommon.enums.OpenType;
import cmc.mellyserver.mellycore.group.application.dto.request.CreateGroupRequestDto;
import cmc.mellyserver.mellycore.group.domain.UserGroup;
import cmc.mellyserver.mellycore.integration.IntegrationTest;
import cmc.mellyserver.mellycore.memory.application.dto.request.CreateMemoryRequestDto;
import cmc.mellyserver.mellycore.memory.application.dto.request.UpdateMemoryRequestDto;
import cmc.mellyserver.mellycore.memory.domain.Memory;
import cmc.mellyserver.mellycore.memory.domain.repository.dto.FindPlaceInfoByMemoryNameResponseDto;
import cmc.mellyserver.mellycore.memory.domain.repository.dto.MemoryResponseDto;
import cmc.mellyserver.mellycore.scrap.application.dto.PlaceResponseDto;
import cmc.mellyserver.mellycore.user.domain.User;
import org.assertj.core.groups.Tuple;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Slice;
import org.springframework.mock.web.MockMultipartFile;

import java.io.IOException;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

public class MemoryServiceIntegrationTest extends IntegrationTest {

    @DisplayName("메모리를 삭제하려고 할때")
    @Nested
    class When_remove_memory {

        @DisplayName("메모리가 존재하면 메모리를 soft-delete 처리한다.")
        @Test
        void delete_memory() {

            // given
            User user = userRepository.save(UserFactory.createEmailLoginUser());

            Memory memory = Memory.builder().title("테스트 제목").content("테스트 컨텐츠")
                    .userId(user.getId()).build();
            Memory savedMemory = memoryRepository.save(memory);

            // when
            memoryService.removeMemory(user.getId(), savedMemory.getId());

            // then
            Memory findMemory = memoryRepository.findById(savedMemory.getId()).get();
            assertThat(findMemory.isDelete()).isTrue();
        }

        @DisplayName("메모리가 없으면 예외를 발생시킨다.")
        @Test
        void memory_not_exist_exception() {

            // given
            User user = userRepository.save(UserFactory.createEmailLoginUser());

            Memory memory = Memory.builder().title("테스트 제목").content("테스트 컨텐츠")
                    .userId(user.getId()).build();
            memoryRepository.save(memory);

            // when then
            assertThatThrownBy(() -> memoryService.removeMemory(user.getId(), 10L))
                    .isInstanceOf(GlobalBadRequestException.class)
                    .hasMessage(ErrorCode.NO_SUCH_MEMORY.getMessage());

        }
    }

    @DisplayName("특정한 메모리의 제목으로 그 메모리가 속한 장소를 찾을 수 있다.")
    @Test
    void get_place_info_by_memory_name() {
        // given
        User user = userRepository.save(UserFactory.createEmailLoginUser());

        CreateMemoryRequestDto createMemoryRequestDto = CreateMemoryRequestDto.builder()
                .id(user.getId()).title("테스트 제목")
                .content("테스트 컨텐츠").placeName("테스트 장소")
                .placeCategory("카페").lat(1.234).lng(1.234)
                .build();

        memoryService.createMemory(createMemoryRequestDto);
        PlaceResponseDto placeByPosition = placeService.findPlaceByPosition(user.getId(),
                1.234, 1.234);

        // when
        List<FindPlaceInfoByMemoryNameResponseDto> memories = memoryService.findPlaceInfoByMemoryName(
                user.getId(), createMemoryRequestDto.getTitle());

        // then
        assertThat(memories).hasSize(1);
        assertThat(memories).extracting("placeId", "memoryName")
                .containsExactlyInAnyOrder(Tuple.tuple(placeByPosition.getPlaceId(), "테스트 제목"));
    }

    @DisplayName("메모리를 수정하려고 할때")
    @Nested
    class When_update_memory {

        @DisplayName("저장된 메모리와 그룹이 있다면 메모리를 수정할 수 있다.")
        @Test
        void update_memory() throws IOException {
            // given
            User user = userRepository.save(UserFactory.createEmailLoginUser());

            MockMultipartFile mockMultipartFile = new MockMultipartFile("testImage", "test",
                    "multipart/form-data", "testImage1".getBytes());

            CreateMemoryRequestDto createMemoryRequestDto = CreateMemoryRequestDto.builder()
                    .id(user.getId()).title("테스트 제목3")
                    .content("테스트 컨텐츠3").placeName("테스트 장소")
                    .keyword(List.of("기뻐요", "좋아요"))
                    .multipartFiles(List.of(mockMultipartFile))
                    .placeCategory("카페").lat(1.234).lng(1.234)
                    .build();

            Memory savedMemory = memoryService.createMemory(createMemoryRequestDto);

            CreateGroupRequestDto createGroupRequestDto = CreateGroupRequestDto.builder()
                    .id(user.getId())
                    .groupName("테스트 그룹")
                    .groupType(GroupType.FRIEND)
                    .groupIcon(1)
                    .build();

            UserGroup userGroup = groupService.saveGroup(createGroupRequestDto);

            UpdateMemoryRequestDto updateMemoryRequestDto = UpdateMemoryRequestDto.builder()
                    .id(user.getId())
                    .memoryId(savedMemory.getId())
                    .groupId(userGroup.getId())
                    .title("수정된 제목")
                    .deleteImageList(List.of(savedMemory.getMemoryImages().get(0).getId()))
                    .content("수정된 컨텐츠")
                    .build();

            // when
            memoryService.updateMemory(updateMemoryRequestDto);

            // then
            Memory findMemory = memoryRepository.findById(updateMemoryRequestDto.getMemoryId())
                    .get();
            assertThat(findMemory.getContent()).isEqualTo("수정된 컨텐츠");
            assertThat(findMemory.getTitle()).isEqualTo("수정된 제목");
            assertThat(findMemory.getMemoryImages()).hasSize(1);
        }

        @DisplayName("저장된 메모리가 없다면 예외가 발생한다.")
        @Test
        void update_memory_memory_not_exist() {
            // given
            User user = userRepository.save(UserFactory.createEmailLoginUser());

            CreateGroupRequestDto createGroupRequestDto = CreateGroupRequestDto.builder()
                    .id(user.getId())
                    .groupName("테스트 그룹")
                    .groupType(GroupType.FRIEND)
                    .groupIcon(1)
                    .build();

            UserGroup userGroup = groupService.saveGroup(createGroupRequestDto);

            UpdateMemoryRequestDto updateMemoryRequestDto = UpdateMemoryRequestDto.builder()
                    .id(user.getId())
                    .memoryId(1L)
                    .groupId(userGroup.getId())
                    .title("수정된 제목")
                    .content("수정된 컨텐츠")
                    .build();

            // when then
            assertThatThrownBy(() -> memoryService.updateMemory(updateMemoryRequestDto))
                    .isInstanceOf(GlobalBadRequestException.class)
                    .hasMessage(ErrorCode.NO_SUCH_MEMORY.getMessage());

        }

        @DisplayName("저장된 그룹이 없다면 예외가 발생한다.")
        @Test
        void update_memory_group_not_exist() {
            // given
            User user = userRepository.save(UserFactory.createEmailLoginUser());

            Memory memory = Memory.builder().title("테스트 제목").content("테스트 컨텐츠")
                    .userId(user.getId()).build();
            Memory savedMemory = memoryRepository.save(memory);

            UpdateMemoryRequestDto updateMemoryRequestDto = UpdateMemoryRequestDto.builder()
                    .id(user.getId())
                    .memoryId(savedMemory.getId())
                    .groupId(1L)
                    .title("수정된 제목")
                    .content("수정된 컨텐츠")
                    .build();

            // when then
            assertThatThrownBy(() -> memoryService.updateMemory(updateMemoryRequestDto))
                    .isInstanceOf(GlobalBadRequestException.class)
                    .hasMessage(ErrorCode.NO_SUCH_GROUP.getMessage());

        }

    }

    @DisplayName("특정 장소에 저장된 메모리를 찾으려 할때")
    @Nested
    class When_find_memories_in_place {

        @DisplayName("현재 로그인한 사용자가 작성한 메모리를 조회한다.")
        @Test
        void get_login_user_write_memories() {

            // given
            User user = userRepository.save(UserFactory.createEmailLoginUser());
            User noneCurrentLoginuser = User.builder()
                    .nickname("test_user2")
                    .password("1234")
                    .ageGroup(AgeGroup.TWO)
                    .build();
            User nonLoginUser = userRepository.save(noneCurrentLoginuser);

            CreateMemoryRequestDto createMemoryRequestDto1 = CreateMemoryRequestDto.builder()
                    .id(user.getId()).title("테스트 제목1")
                    .content("테스트 컨텐츠1").placeName("테스트 장소")
                    .keyword(List.of("기뻐요", "좋아요"))
                    .placeCategory("카페").lat(1.234).lng(1.234)
                    .build();

            CreateMemoryRequestDto createMemoryRequestDto2 = CreateMemoryRequestDto.builder()
                    .id(user.getId()).title("테스트 제목2")
                    .content("테스트 컨텐츠2").placeName("테스트 장소")
                    .keyword(List.of("기뻐요", "좋아요"))
                    .placeCategory("카페").lat(1.234).lng(1.234)
                    .build();

            CreateMemoryRequestDto createMemoryRequestDto3 = CreateMemoryRequestDto.builder()
                    .id(nonLoginUser.getId()).title("테스트 제목3")
                    .content("테스트 컨텐츠3").placeName("테스트 장소")
                    .keyword(List.of("기뻐요", "좋아요"))
                    .placeCategory("카페").lat(1.234).lng(1.234)
                    .build();

            memoryService.createMemory(createMemoryRequestDto1);
            memoryService.createMemory(createMemoryRequestDto2);
            memoryService.createMemory(createMemoryRequestDto3);

            PlaceResponseDto place = placeService.findPlaceByPosition(user.getId(), 1.234,
                    1.234);

            // when
            Slice<MemoryResponseDto> loginUserWriteMemoryBelongToPlace = memoryService.findLoginUserWriteMemoryBelongToPlace(
                    PageRequest.of(0, 10), user.getId(), place.getPlaceId(), null);

            // then
            assertThat(loginUserWriteMemoryBelongToPlace.getContent()).hasSize(2);
            assertThat(loginUserWriteMemoryBelongToPlace.getContent()).extracting("title",
                            "content")
                    .containsExactlyInAnyOrder(Tuple.tuple("테스트 제목1", "테스트 컨텐츠1"),
                            Tuple.tuple("테스트 제목2", "테스트 컨텐츠2"));
        }

        @DisplayName("현재 로그인한 사용자 이외의 사용자가 작성한 메모리를 조회한다.")
        @Test
        void get_group_member_write_memories() {

            // given
            User user = userRepository.save(UserFactory.createEmailLoginUser());
            User noneCurrentLoginuser = User.builder()
                    .nickname("test_user2")
                    .password("1234")
                    .ageGroup(AgeGroup.TWO)
                    .build();
            User nonLoginUser = userRepository.save(noneCurrentLoginuser);

            CreateMemoryRequestDto createMemoryRequestDto1 = CreateMemoryRequestDto.builder()
                    .id(user.getId()).title("테스트 제목1")
                    .content("테스트 컨텐츠1").placeName("테스트 장소")
                    .keyword(List.of("기뻐요", "좋아요"))
                    .placeCategory("카페").lat(1.234).lng(1.234)
                    .build();

            CreateMemoryRequestDto createMemoryRequestDto2 = CreateMemoryRequestDto.builder()
                    .id(user.getId()).title("테스트 제목2")
                    .content("테스트 컨텐츠2").placeName("테스트 장소")
                    .keyword(List.of("기뻐요", "좋아요"))
                    .placeCategory("카페").lat(1.234).lng(1.234)
                    .build();

            CreateMemoryRequestDto createMemoryRequestDto3 = CreateMemoryRequestDto.builder()
                    .id(nonLoginUser.getId()).title("테스트 제목3")
                    .content("테스트 컨텐츠3").placeName("테스트 장소")
                    .keyword(List.of("기뻐요", "좋아요"))
                    .placeCategory("카페").lat(1.234).lng(1.234)
                    .build();

            memoryService.createMemory(createMemoryRequestDto1);
            memoryService.createMemory(createMemoryRequestDto2);
            memoryService.createMemory(createMemoryRequestDto3);

            PlaceResponseDto place = placeService.findPlaceByPosition(user.getId(), 1.234,
                    1.234);

            // when
            Slice<MemoryResponseDto> loginUserWriteMemoryBelongToPlace = memoryService.findOtherUserWriteMemoryBelongToPlace(
                    PageRequest.of(0, 10), user.getId(), place.getPlaceId(), null);

            // then
            assertThat(loginUserWriteMemoryBelongToPlace.getContent()).hasSize(1);
            assertThat(loginUserWriteMemoryBelongToPlace.getContent()).extracting("title",
                            "content")
                    .containsExactlyInAnyOrder(Tuple.tuple("테스트 제목3", "테스트 컨텐츠3"));
        }

        @DisplayName("현재 로그인한 사용자와 같은 그룹의 사용자가 작성한 메모리를 조회한다.")
        @Test
        void get_other_user_write_memories() {

            // given
            User user = userRepository.save(UserFactory.createEmailLoginUser());
            User nonLoginUser = userRepository.save(
                    User.builder().nickname("test_user2").password("1234").ageGroup(AgeGroup.TWO)
                            .build());

            CreateGroupRequestDto createGroupRequestDto = CreateGroupRequestDto.builder()
                    .id(user.getId())
                    .groupName("테스트 그룹")
                    .groupType(GroupType.FRIEND)
                    .groupIcon(1)
                    .build();
            UserGroup userGroup = groupService.saveGroup(createGroupRequestDto);

            groupService.participateToGroup(user.getId(), userGroup.getId());
            groupService.participateToGroup(nonLoginUser.getId(), userGroup.getId());

            CreateMemoryRequestDto createMemoryRequestDto1 = CreateMemoryRequestDto.builder()
                    .id(user.getId()).title("테스트 제목1")
                    .groupId(userGroup.getId())
                    .content("테스트 컨텐츠1").placeName("테스트 장소")
                    .keyword(List.of("기뻐요", "좋아요"))
                    .openType(OpenType.GROUP)
                    .placeCategory("카페").lat(1.234).lng(1.234)
                    .build();

            CreateMemoryRequestDto createMemoryRequestDto2 = CreateMemoryRequestDto.builder()
                    .id(nonLoginUser.getId()).title("테스트 제목2")
                    .content("테스트 컨텐츠2").placeName("테스트 장소")
                    .groupId(userGroup.getId())
                    .keyword(List.of("기뻐요", "좋아요"))
                    .openType(OpenType.GROUP)
                    .placeCategory("카페").lat(1.234).lng(1.234)
                    .build();

            CreateMemoryRequestDto createMemoryRequestDto3 = CreateMemoryRequestDto.builder()
                    .id(nonLoginUser.getId()).title("테스트 제목3")
                    .content("테스트 컨텐츠3").placeName("테스트 장소")
                    .groupId(userGroup.getId())
                    .keyword(List.of("기뻐요", "좋아요"))
                    .openType(OpenType.GROUP)
                    .placeCategory("카페").lat(1.234).lng(1.234)
                    .build();

            memoryService.createMemory(createMemoryRequestDto1);
            memoryService.createMemory(createMemoryRequestDto2);
            memoryService.createMemory(createMemoryRequestDto3);

            PlaceResponseDto place = placeService.findPlaceByPosition(user.getId(), 1.234,
                    1.234);

            // when
            Slice<MemoryResponseDto> loginUserWriteMemoryBelongToPlace = memoryService.findMyGroupMemberWriteMemoryBelongToPlace(
                    PageRequest.of(0, 10), user.getId(), place.getPlaceId(), null);

            // then
            assertThat(loginUserWriteMemoryBelongToPlace.getContent()).hasSize(2);

        }

    }

    @DisplayName("메모리를 새로 작성하고, 아직 장소 정보가 DB에 없을때 함께하는 그룹으로 설정한 그룹이 DB에 존재하지 않으면 예외를 발생시킨다.")
    @Test
    void create_memory_exception() {

        // given
        User user = userRepository.save(UserFactory.createEmailLoginUser());
        User nonLoginUser = userRepository.save(
                User.builder().nickname("test_user2").password("1234").ageGroup(AgeGroup.TWO)
                        .build());

        CreateGroupRequestDto createGroupRequestDto = CreateGroupRequestDto.builder()
                .id(user.getId())
                .groupName("테스트 그룹")
                .groupType(GroupType.FRIEND)
                .groupIcon(1)
                .build();

        UserGroup userGroup = groupService.saveGroup(createGroupRequestDto);

        groupService.participateToGroup(user.getId(), userGroup.getId());
        groupService.participateToGroup(nonLoginUser.getId(), userGroup.getId());

        CreateMemoryRequestDto createMemoryRequestDto1 = CreateMemoryRequestDto.builder()
                .id(user.getId()).title("테스트 제목1")
                .groupId(3L)
                .content("테스트 컨텐츠1").placeName("테스트 장소")
                .keyword(List.of("기뻐요", "좋아요"))
                .openType(OpenType.GROUP)
                .placeCategory("카페").lat(1.234).lng(1.234)
                .build();

        // when then
        assertThatThrownBy(() -> memoryService.createMemory(createMemoryRequestDto1))
                .isInstanceOf(GlobalBadRequestException.class)
                .hasMessage(ErrorCode.NO_SUCH_GROUP.getMessage());

    }

    @DisplayName("메모리를 새로 작성하고, 장소 정보가 DB에 있을때 함께하는 그룹으로 설정한 그룹이 DB에 존재하지 않으면 예외를 발생시킨다.")
    @Test
    void create_memory_with_place_exception() {

        // given
        User user = userRepository.save(UserFactory.createEmailLoginUser());
        User nonLoginUser = userRepository.save(
                User.builder().nickname("test_user2").password("1234").ageGroup(AgeGroup.TWO)
                        .build());

        CreateGroupRequestDto createGroupRequestDto = CreateGroupRequestDto.builder()
                .id(user.getId())
                .groupName("테스트 그룹")
                .groupType(GroupType.FRIEND)
                .groupIcon(1)
                .build();

        UserGroup userGroup = groupService.saveGroup(createGroupRequestDto);

        groupService.participateToGroup(user.getId(), userGroup.getId());
        groupService.participateToGroup(nonLoginUser.getId(), userGroup.getId());

        CreateMemoryRequestDto resultWithExistGroup = CreateMemoryRequestDto.builder()
                .id(user.getId()).title("테스트 제목1")
                .groupId(userGroup.getId())
                .content("테스트 컨텐츠1").placeName("테스트 장소")
                .keyword(List.of("기뻐요", "좋아요"))
                .openType(OpenType.GROUP)
                .placeCategory("카페").lat(1.234).lng(1.234)
                .build();

        memoryService.createMemory(resultWithExistGroup);

        // when then
        CreateMemoryRequestDto resultWithoutExistGroup = CreateMemoryRequestDto.builder()
                .id(user.getId()).title("테스트 제목1")
                .groupId(3L)
                .content("테스트 컨텐츠1").placeName("테스트 장소")
                .keyword(List.of("기뻐요", "좋아요"))
                .openType(OpenType.GROUP)
                .placeCategory("카페").lat(1.234).lng(1.234)
                .build();

        assertThatThrownBy(() -> memoryService.createMemory(resultWithoutExistGroup))
                .isInstanceOf(GlobalBadRequestException.class)
                .hasMessage(ErrorCode.NO_SUCH_GROUP.getMessage());

    }

}
