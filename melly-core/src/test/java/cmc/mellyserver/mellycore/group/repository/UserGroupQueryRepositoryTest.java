package cmc.mellyserver.mellycore.group.repository;

import cmc.mellyserver.mellycore.common.RepositoryTest;

public class UserGroupQueryRepositoryTest extends RepositoryTest {

//    @Autowired
//    private UserGroupQueryRepository userGroupQueryRepository;
//
//    @Autowired
//    private UserRepository userRepository;
//
//    @Autowired
//    private MemoryRepository memoryRepository;
//
//    @Autowired
//    private GroupRepository groupRepository;
//
//    @Autowired
//    private GroupAndUserRepository groupAndUserRepository;
//    @Autowired
//    private TestEntityManager testEntityManager;
//
//    private User 제민;
//    private User 재현;
//
//    private List<UserGroup> groups;
//
//    @BeforeEach
//    void setUp() {
//
//        // given
//        User user1 = fixture.UserFixtures.제민();
//        제민 = userRepository.save(user1);
//
//        User user2 = fixture.UserFixtures.재현();
//        재현 = userRepository.save(user2);
//
//        UserGroup 친구_그룹 = UserGroup.cmc.mellyserver.mellycore.builder().groupName("친구")
//                .groupType(GroupType.FRIEND).build();
//        GroupAndUser 친구_그룹_연결_1 = new GroupAndUser(제민, 친구_그룹);
//        GroupAndUser 친구_그룹_연결_2 = new GroupAndUser(재현, 친구_그룹);
//
//        UserGroup 가족_그룹 = UserGroup.cmc.mellyserver.mellycore.builder().groupName("가족")
//                .groupType(GroupType.FAMILY).build();
//        GroupAndUser 가족_그룹_연결 = new GroupAndUser(제민, 가족_그룹);
//
//        UserGroup 동료_그룹 = UserGroup.cmc.mellyserver.mellycore.builder().groupName("동료")
//                .groupType(GroupType.COMPANY).build();
//        GroupAndUser 동료_그룹_연결 = new GroupAndUser(제민, 동료_그룹);
//
//        groupAndUserRepository.saveAll(List.of(친구_그룹_연결_1, 친구_그룹_연결_2, 가족_그룹_연결, 동료_그룹_연결));
//        groups = groupRepository.saveAll(List.of(친구_그룹, 가족_그룹, 동료_그룹));
//
//        testEntityManager.flush();
//        testEntityManager.clear();
//
//
//    }
//
//    @DisplayName("메모리를 등록하기 위해서 내가 속한 그룹 리스트를 조회한다.")
//    @Test
//    void 메모리_등록을_위해서_내가_속한_그룹들을_조회한다() {
//
//        // when
//        List<GroupListForSaveMemoryResponseDto> userGroupListForMemoryEnroll = userGroupQueryRepository.getUserGroupListForMemoryEnroll(
//                제민.getId());
//
//        // then
//        assertThat(userGroupListForMemoryEnroll).hasSize(3);
//        assertThat(userGroupListForMemoryEnroll).extracting("groupName")
//                .containsExactlyInAnyOrder("친구", "가족", "동료");
//    }
//
//    @DisplayName("로그인한 유저가 속해있는 그룹을 모두 조회한다.")
//    @Test
//    void 로그인한_유저가_속해있는_그룹을_모두_반환한다() {
//
//        // when
//        List<GroupLoginUserParticipatedResponseDto> groupListLoginUserParticipate = userGroupQueryRepository.getGroupListLoginUserParticipate(
//                제민.getId());
//
//        // then
//        assertThat(groupListLoginUserParticipate).hasSize(3);
//        assertThat(groupListLoginUserParticipate).extracting("groupName")
//                .containsExactlyInAnyOrder("친구", "가족", "동료");
//    }


}
