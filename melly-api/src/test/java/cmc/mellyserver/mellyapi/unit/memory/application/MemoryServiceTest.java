package cmc.mellyserver.mellyapi.unit.memory.application;

import cmc.mellyserver.mellycommon.codes.ErrorCode;
import cmc.mellyserver.mellycommon.enums.GroupType;
import cmc.mellyserver.mellycore.common.AuthenticatedUserChecker;
import cmc.mellyserver.mellycore.common.exception.GlobalBadRequestException;
import cmc.mellyserver.mellycore.group.domain.repository.GroupAndUserRepository;
import cmc.mellyserver.mellycore.group.domain.repository.GroupRepository;
import cmc.mellyserver.mellycore.group.domain.repository.UserGroupQueryRepository;
import cmc.mellyserver.mellycore.group.domain.repository.dto.GroupListForSaveMemoryResponseDto;
import cmc.mellyserver.mellycore.memory.application.MemoryService;
import cmc.mellyserver.mellycore.memory.domain.Memory;
import cmc.mellyserver.mellycore.memory.domain.repository.MemoryQueryRepository;
import cmc.mellyserver.mellycore.memory.domain.repository.MemoryRepository;
import cmc.mellyserver.mellycore.memory.domain.repository.dto.MemoryResponseDto;
import cmc.mellyserver.mellycore.place.domain.repository.PlaceRepository;
import org.assertj.core.groups.Tuple;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.BDDMockito.*;

@ExtendWith(MockitoExtension.class)
public class MemoryServiceTest {

    @InjectMocks
    private MemoryService memoryService;

    @Mock
    private UserGroupQueryRepository userGroupQueryRepository;

    @Mock
    private AuthenticatedUserChecker authenticatedUserChecker;

    @Mock
    private MemoryRepository memoryRepository;

    @Mock
    private MemoryQueryRepository memoryQueryRepository;

    @Mock
    private PlaceRepository placeRepository;

    @Mock
    private FileUploader fileUploader;

    @Mock
    private GroupRepository groupRepository;

    @Mock
    private GroupAndUserRepository groupAndUserRepository;

    @DisplayName("메모리 추가를 위해 로그인 유저가 참여하고 있는 그룹 리스트 조회 가능하다")
    @Test
    void get_group_list_for_add_memory() {

        // given
        GroupListForSaveMemoryResponseDto groupListForSaveMemoryResponseDto1 = GroupListForSaveMemoryResponseDto.builder()
                .groupName("그룹 1")
                .groupType(GroupType.FRIEND)
                .build();
        GroupListForSaveMemoryResponseDto groupListForSaveMemoryResponseDto2 = GroupListForSaveMemoryResponseDto.builder()
                .groupName("그룹 2")
                .groupType(GroupType.FAMILY)
                .build();
        GroupListForSaveMemoryResponseDto groupListForSaveMemoryResponseDto3 = GroupListForSaveMemoryResponseDto.builder()
                .groupName("그룹 3")
                .groupType(GroupType.COMPANY)
                .build();
        List<GroupListForSaveMemoryResponseDto> list = List.of(groupListForSaveMemoryResponseDto1,
                groupListForSaveMemoryResponseDto2, groupListForSaveMemoryResponseDto3);

        given(userGroupQueryRepository.getUserGroupListForMemoryEnroll(anyLong())).willReturn(list);

        // when
        List<GroupListForSaveMemoryResponseDto> groupListLoginUserParticipate = memoryService.findGroupListLoginUserParticipate(
                1L);

        // then
        assertThat(groupListLoginUserParticipate)
                .extracting("groupName", "groupType")
                .containsExactlyInAnyOrder(Tuple.tuple("그룹 1", GroupType.FRIEND),
                        Tuple.tuple("그룹 2", GroupType.FAMILY),
                        Tuple.tuple("그룹 3", GroupType.COMPANY));

        verify(userGroupQueryRepository, times(1)).getUserGroupListForMemoryEnroll(anyLong());
    }

    @DisplayName("메모리 식별자를 통해 메모리 조회 가능하다.")
    @Test
    void find_memory_by_memoryId() {

        // given
        MemoryResponseDto memoryResponseDto = MemoryResponseDto.builder().title("메모리 제목")
                .content("메모리 컨텐츠").build();
        given(memoryQueryRepository.getMemoryByMemoryId(anyLong(), anyLong())).willReturn(
                memoryResponseDto);

        // when
        MemoryResponseDto memoryByMemoryId = memoryService.findMemoryByMemoryId(1L, 1L);

        // then
        assertThat(memoryByMemoryId)
                .usingRecursiveComparison()
                .isEqualTo(memoryResponseDto);

        verify(memoryQueryRepository, times(1)).getMemoryByMemoryId(anyLong(), anyLong());
    }

    @DisplayName("해당 장소에 로그인한 유저가 작성한 메모리 리스트를 조회할 수 있다.")
    @Test
    void get_memories_login_user_write_in_place() {
        // given

        // when

        // then
    }

    @DisplayName("해당 장소에 다른 유저가 작성한 메모리 리스트를 조회할 수 있다.")
    @Test
    void get_memories_other_user_write_in_place() {
        // given

        // when

        // then
    }

    @DisplayName("해당 장소에 내 그룹에 속한 유저가 작성한 메모리 리스트를 조회할 수 있다.")
    @Test
    void get_memories_belong_to_my_group_user_write_in_place() {
        // given

        // when

        // then
    }

    @DisplayName("메모리를 삭제할 때")
    @Nested
    class When_remove_memory {

        @DisplayName("메모리가 존재하지 않으면 예외가 발생한다.")
        @Test
        void memory_not_exist_exception() {
            // given
            given(memoryRepository.findById(anyLong())).willReturn(Optional.empty());

            // when // then
            assertThatThrownBy(() -> memoryService.removeMemory(1L, 1L))
                    .isInstanceOf(GlobalBadRequestException.class)
                    .hasMessage(ErrorCode.NO_SUCH_MEMORY.getMessage());
        }

        @DisplayName("메모리가 존재하면 삭제가 가능하다.")
        @Test
        void memory_remove() {
            // given
            Memory memory = Memory.builder().title("메모리 제목").content("메모리 컨텐츠").isDelete(false)
                    .build();

            given(memoryRepository.findById(anyLong())).willReturn(Optional.of(memory));

            // when
            memoryService.removeMemory(1L, 1L);

            // then
            assertThat(memory.isDelete()).isTrue();
            verify(memoryRepository, times(1)).findById(anyLong());
        }
    }

}
