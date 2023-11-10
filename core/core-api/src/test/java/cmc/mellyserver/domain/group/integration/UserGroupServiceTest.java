package cmc.mellyserver.domain.group.integration;

import static cmc.mellyserver.fixtures.UserFixtures.*;

import java.util.List;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;

import cmc.mellyserver.dbcore.group.GroupAndUserRepository;
import cmc.mellyserver.dbcore.group.GroupRepository;
import cmc.mellyserver.dbcore.group.UserGroup;
import cmc.mellyserver.dbcore.group.enums.GroupType;
import cmc.mellyserver.dbcore.user.GroupMemberResponseDto;
import cmc.mellyserver.dbcore.user.User;
import cmc.mellyserver.dbcore.user.UserRepository;
import cmc.mellyserver.domain.group.GroupService;
import cmc.mellyserver.domain.group.dto.request.CreateGroupRequestDto;
import cmc.mellyserver.domain.group.dto.request.UpdateGroupRequestDto;
import cmc.mellyserver.domain.group.dto.response.GroupListLoginUserParticipatedResponse;
import cmc.mellyserver.domain.group.query.dto.GroupDetailResponseDto;
import cmc.mellyserver.fixtures.GroupFixtures;
import cmc.mellyserver.support.IntegrationTestSupport;
import cmc.mellyserver.support.exception.BusinessException;
import cmc.mellyserver.support.exception.ErrorCode;

public class UserGroupServiceTest extends IntegrationTestSupport {

	@Autowired
	private GroupService groupService;

	@Autowired
	private GroupRepository groupRepository;

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private GroupAndUserRepository groupAndUserRepository;

	@DisplayName("그룹 세부 정보를 조회한다")
	@Test
	void 그룹_세부정보를_조회한다() {

		// given
		User 모카 = userRepository.save(모카());
		User 머식 = userRepository.save(머식());

		UserGroup 친구들 = groupRepository.save(GroupFixtures.친구그룹());
		groupService.joinGroup(모카.getId(), 친구들.getId());
		groupService.joinGroup(머식.getId(), 친구들.getId());

		// when
		GroupDetailResponseDto groupDetail = groupService.getGroupDetail(친구들.getId(), 모카.getId());

		// then
		GroupMemberResponseDto 모카_응답 = GroupMemberResponseDto.of(모카.getId(), 모카.getProfileImage(), 모카.getNickname(),
			true);
		GroupMemberResponseDto 머식_응답 = GroupMemberResponseDto.of(머식.getId(), 머식.getProfileImage(), 머식.getNickname(),
			false);

		Assertions.assertThat(groupDetail.getGroupName()).isEqualTo(친구들.getGroupName());
		Assertions.assertThat(groupDetail.getGroupMembers()).hasSize(2);
		Assertions.assertThat(groupDetail.getGroupMembers())
			.usingRecursiveComparison()
			.isEqualTo(List.of(모카_응답, 머식_응답));
	}

	@DisplayName("내가 속한 그룹 리스트를 조회한다")
	@Test
	void 내가_속한_그룹_리스트를_조회한다() {

		// given
		User 모카 = userRepository.save(모카());

		UserGroup 친구들 = groupRepository.save(GroupFixtures.친구그룹());
		UserGroup 가족 = groupRepository.save(GroupFixtures.가족그룹());
		groupService.joinGroup(모카.getId(), 친구들.getId());
		groupService.joinGroup(모카.getId(), 가족.getId());

		// when
		GroupListLoginUserParticipatedResponse userParticipatedGroups = groupService.findUserParticipatedGroups(
			모카.getId(), -1L,
			PageRequest.of(0, 10));

		// then
		Assertions.assertThat(userParticipatedGroups.getContents()).hasSize(2);
	}

	@DisplayName("그룹을 생성한다")
	@Test
	void 그룹을_생성한다() {

		// given
		User 모카 = userRepository.save(모카());
		CreateGroupRequestDto createGroupRequestDto = CreateGroupRequestDto.builder()
			.groupName("친구들")
			.groupIcon(1)
			.id(모카.getId())
			.groupType(
				GroupType.FRIEND)
			.build();

		// when
		groupService.saveGroup(createGroupRequestDto);

		// then
		List<UserGroup> result = groupRepository.findAll();
		Assertions.assertThat(result).hasSize(1);
	}

	@DisplayName("그룹을 수정한다")
	@Test
	void 그룹을_수정한다() {

		// given
		UserGroup 친구들 = groupRepository.save(GroupFixtures.친구그룹());
		UpdateGroupRequestDto updateGroupRequestDto = UpdateGroupRequestDto.builder()
			.groupId(친구들.getId())
			.groupName("수정된 이름")
			.groupType(GroupType.COMPANY)
			.groupIcon(2)
			.build();

		// when
		groupService.updateGroup(updateGroupRequestDto);

		// then
		UserGroup userGroup = groupRepository.findById(친구들.getId()).get();
		Assertions.assertThat(userGroup.getGroupName()).isEqualTo(updateGroupRequestDto.getGroupName());
	}

	@DisplayName("그룹을 삭제한다")
	@Test
	void 그룹을_삭제한다() {

		// given
		UserGroup 친구들 = groupRepository.save(GroupFixtures.친구그룹());

		// when
		groupService.removeGroup(친구들.getId());

		// then
		UserGroup userGroup = groupRepository.findById(친구들.getId()).get();
		Assertions.assertThat(userGroup.getIsDeleted()).isTrue();
	}

	@DisplayName("그룹에 참여한다")
	@Test
	void 그룹에_참여한다() {

		// given
		UserGroup 친구들 = groupRepository.save(GroupFixtures.친구그룹());
		User 모카 = userRepository.save(모카());

		// when
		groupService.joinGroup(모카.getId(), 친구들.getId());

		// then
		boolean isPresent = groupAndUserRepository.findByUserIdAndGroupId(모카.getId(), 친구들.getId()).isPresent();
		Assertions.assertThat(isPresent).isTrue();
	}

	@DisplayName("그룹의_인원은_10명을_초과할수없다")
	@Test
	void 그룹의_인원은_10명을_초과할수없다() {

		// Given
		UserGroup 친구들 = groupRepository.save(GroupFixtures.친구그룹());

		for (int i = 0; i < 10; i++) {
			User 모카 = userRepository.save(모카());
			groupService.joinGroup(모카.getId(), 친구들.getId());
		}

		// when & then
		User 마지막_회원 = userRepository.save(모카());

		Assertions.assertThatThrownBy(() -> {
			groupService.joinGroup(마지막_회원.getId(), 친구들.getId());
		}).isInstanceOf(BusinessException.class).hasMessage(ErrorCode.PARTICIPATE_GROUP_NOT_POSSIBLE.getMessage());
	}

	@DisplayName("그룹을_탈퇴한다")
	@Test
	void 그룹을_탈퇴한다() {

		// given
		UserGroup 친구들 = groupRepository.save(GroupFixtures.친구그룹());
		User 모카 = userRepository.save(모카());
		User 머식 = userRepository.save(머식());
		groupService.joinGroup(모카.getId(), 친구들.getId());
		groupService.joinGroup(머식.getId(), 친구들.getId());

		// when
		groupService.exitGroup(모카.getId(), 친구들.getId());

		// then
		boolean isExited = groupAndUserRepository.findByUserIdAndGroupId(모카.getId(), 친구들.getId()).isEmpty();
		Assertions.assertThat(isExited).isTrue();
	}

	// @DisplayName("분산락을 적용해 동시 접속자가 생겨도 10명 인원제한을 유지한다")
	// @Test
	// void 분산락을_적용해_동시접속자가_생겨도_10명제한을_유지한다() throws InterruptedException {
	//
	// 	// Given
	// 	UserGroup 친구들 = groupRepository.save(GroupFixtures.친구그룹());
	//
	// 	for (int i = 0; i < 9; i++) {
	// 		User 모카 = userRepository.save(모카());
	// 		groupService.participateToGroup(모카.getId(), 친구들.getId());
	// 	}
	//
	// 	int numberOfThreads = 2;
	// 	ExecutorService service = Executors.newFixedThreadPool(numberOfThreads);
	// 	CountDownLatch latch = new CountDownLatch(numberOfThreads);
	//
	// 	// when
	// 	User 동시접속유저_1 = userRepository.save(모카());
	// 	User 동시접속유저_2 = userRepository.save(모카());
	//
	// 	userRepository.flush();
	//
	// 	service.execute(() -> {
	// 		groupService.participateToGroup(동시접속유저_1.getId(), 친구들.getId());
	// 		latch.countDown();
	// 	});
	//
	// 	service.execute(() -> {
	// 		groupService.participateToGroup(동시접속유저_2.getId(), 친구들.getId());
	// 		latch.countDown();
	// 	});
	//
	// 	latch.await();
	//
	// 	// then
	// 	Integer count = groupAndUserRepository.countUserParticipatedInGroup(친구들.getId());
	// 	Assertions.assertThat(count).isEqualTo(10);
	// }

}
