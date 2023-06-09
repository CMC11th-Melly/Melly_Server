package cmc.mellyserver.mellycore.unit.group.repository;

import static org.assertj.core.api.Assertions.assertThat;

import cmc.mellyserver.mellycore.common.enums.GroupType;
import cmc.mellyserver.mellycore.common.enums.OpenType;
import cmc.mellyserver.mellycore.group.domain.GroupAndUser;
import cmc.mellyserver.mellycore.group.domain.UserGroup;
import cmc.mellyserver.mellycore.group.domain.repository.GroupAndUserRepository;
import cmc.mellyserver.mellycore.group.domain.repository.GroupRepository;
import cmc.mellyserver.mellycore.group.domain.repository.UserGroupQueryRepository;
import cmc.mellyserver.mellycore.group.domain.repository.dto.GroupListForSaveMemoryResponseDto;
import cmc.mellyserver.mellycore.group.domain.repository.dto.GroupLoginUserParticipatedResponseDto;
import cmc.mellyserver.mellycore.memory.domain.Memory;
import cmc.mellyserver.mellycore.memory.domain.repository.MemoryRepository;
import cmc.mellyserver.mellycore.memory.domain.repository.dto.MemoryResponseDto;
import cmc.mellyserver.mellycore.memory.domain.vo.GroupInfo;
import cmc.mellyserver.mellycore.unit.RepositoryTest;
import cmc.mellyserver.mellycore.unit.common.fixtures.UserFixtures;
import cmc.mellyserver.mellycore.user.domain.User;
import cmc.mellyserver.mellycore.user.domain.repository.UserRepository;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Slice;

public class UserGroupQueryRepositoryTest extends RepositoryTest {

    @Autowired
    private UserGroupQueryRepository userGroupQueryRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private MemoryRepository memoryRepository;

    @Autowired
    private GroupRepository groupRepository;

    @Autowired
    private GroupAndUserRepository groupAndUserRepository;
    @Autowired
    private TestEntityManager testEntityManager;

    private User 제민;
    private User 재현;

    private List<UserGroup> groups;

    @BeforeEach
    void setUp() {

        // given
        User user1 = UserFixtures.제민();
        제민 = userRepository.save(user1);

        User user2 = UserFixtures.재현();
        재현 = userRepository.save(user2);

        UserGroup 친구_그룹 = UserGroup.builder().groupName("친구")
                .groupType(GroupType.FRIEND).build();
        GroupAndUser 친구_그룹_연결_1 = new GroupAndUser(제민, 친구_그룹);
        GroupAndUser 친구_그룹_연결_2 = new GroupAndUser(재현, 친구_그룹);

        UserGroup 가족_그룹 = UserGroup.builder().groupName("가족")
                .groupType(GroupType.FAMILY).build();
        GroupAndUser 가족_그룹_연결 = new GroupAndUser(제민, 가족_그룹);

        UserGroup 동료_그룹 = UserGroup.builder().groupName("동료")
                .groupType(GroupType.COMPANY).build();
        GroupAndUser 동료_그룹_연결 = new GroupAndUser(제민, 동료_그룹);

        groupAndUserRepository.saveAll(List.of(친구_그룹_연결_1, 친구_그룹_연결_2, 가족_그룹_연결, 동료_그룹_연결));
        groups = groupRepository.saveAll(List.of(친구_그룹, 가족_그룹, 동료_그룹));

        testEntityManager.flush();
        testEntityManager.clear();


    }

    @DisplayName("메모리를 등록하기 위해서 내가 속한 그룹 리스트를 조회한다.")
    @Test
    void 메모리_등록을_위해서_내가_속한_그룹들을_조회한다() {

        // when
        List<GroupListForSaveMemoryResponseDto> userGroupListForMemoryEnroll = userGroupQueryRepository.getUserGroupListForMemoryEnroll(
                제민.getUserSeq());

        // then
        assertThat(userGroupListForMemoryEnroll).hasSize(3);
        assertThat(userGroupListForMemoryEnroll).extracting("groupName")
                .containsExactlyInAnyOrder("친구", "가족", "동료");
    }

    @DisplayName("로그인한 유저가 속해있는 그룹을 모두 조회한다.")
    @Test
    void 로그인한_유저가_속해있는_그룹을_모두_반환한다() {

        // when
        List<GroupLoginUserParticipatedResponseDto> groupListLoginUserParticipate = userGroupQueryRepository.getGroupListLoginUserParticipate(
                제민.getUserSeq());

        // then
        assertThat(groupListLoginUserParticipate).hasSize(3);
        assertThat(groupListLoginUserParticipate).extracting("groupName")
                .containsExactlyInAnyOrder("친구", "가족", "동료");
    }

    @DisplayName("로그인한 유저가 속한 그룹에서 작성한 메모리 중 전체공개와 그룹공개는 조회하고 비공개는 조회하지 않는다.")
    @ParameterizedTest
    @CsvSource({"ALL,2", "GROUP,2", "PRIVATE,0"})
    void 로그인_유저가_속한_그룹에서_작성한_메모리를_모두_조회한다(OpenType openType, int memoryCount) {

        // given
        Memory memory_제민 = Memory.builder().title("테스트 메모리").userId(제민.getUserSeq())
                .content("테스트 본문")
                .groupInfo(new GroupInfo("친구", GroupType.FRIEND, groups.get(0).getId()))
                .openType(openType).build();

        memory_제민.setKeyword(List.of("좋아요"));

        Memory memory_재현 = Memory.builder().title("테스트 메모리").userId(제민.getUserSeq())
                .content("테스트 본문")
                .groupInfo(new GroupInfo("친구", GroupType.FRIEND, groups.get(0).getId()))
                .openType(openType).build();
        memory_재현.setKeyword(List.of("좋아요"));

        memoryRepository.saveAll(List.of(memory_재현, memory_제민));

        // when
        Slice<MemoryResponseDto> myGroupMemory = userGroupQueryRepository.getMyGroupMemory(
                PageRequest.of(0, 10), groups.get(0).getId(), 제민.getUserSeq());

        // then
        assertThat(myGroupMemory.getContent()).hasSize(memoryCount);

    }
}
