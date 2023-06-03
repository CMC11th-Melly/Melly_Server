package cmc.mellyserver.mellyapi.integration.user;

import static org.assertj.core.api.Assertions.*;

import java.time.LocalDateTime;
import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Slice;

import cmc.mellyserver.mellyapi.common.exception.GlobalBadRequestException;
import cmc.mellyserver.mellyapi.common.factory.UserFactory;
import cmc.mellyserver.mellyapi.integration.IntegrationTest;
import cmc.mellyserver.mellycore.common.enums.GroupType;
import cmc.mellyserver.mellycore.common.enums.OpenType;
import cmc.mellyserver.mellycore.group.domain.UserGroup;
import cmc.mellyserver.mellycore.group.domain.repository.dto.GroupLoginUserParticipatedResponseDto;
import cmc.mellyserver.mellycore.memory.domain.Memory;
import cmc.mellyserver.mellycore.memory.domain.MemoryImage;
import cmc.mellyserver.mellycore.memory.domain.repository.dto.MemoryResponseDto;
import cmc.mellyserver.mellycore.memory.domain.vo.GroupInfo;
import cmc.mellyserver.mellycore.user.domain.User;

public class UserServiceIntegrationTest extends IntegrationTest {

	@DisplayName("사용자가 속한 그룹 리스트를 조회한다.")
	@Test
	void get_group_list_user_participated() {
		// given
		User user = userRepository.save(UserFactory.createEmailLoginUser());

		UserGroup group1 = groupRepository.save(new UserGroup("테스트그룹1", "invitationLink", GroupType.FRIEND, 1, 1L));
		UserGroup group2 = groupRepository.save(new UserGroup("테스트그룹2", "invitationLink", GroupType.FAMILY, 2, 2L));
		UserGroup group3 = groupRepository.save(new UserGroup("테스트그룹3", "invitationLink", GroupType.COMPANY, 3, 3L));

		userService.participateToGroup(user.getUserSeq(), group1.getId());
		userService.participateToGroup(user.getUserSeq(), group2.getId());
		userService.participateToGroup(user.getUserSeq(), group3.getId());

		// when
		List<GroupLoginUserParticipatedResponseDto> result = userService.findGroupListLoginUserParticiated(
			user.getUserSeq());

		// then
		assertThat(result).hasSize(3)
			.extracting("groupName")
			.containsExactlyInAnyOrder("테스트그룹1", "테스트그룹2", "테스트그룹3");

	}

	@DisplayName("사용자가 그룹에 참여하려고 할때")
	@Nested
	class When_user_want_to_participate_to_group {
		@DisplayName("존재하지 않는 그룹에 참여할 수는 없다. - 400 예외")
		@Test
		void participate_Invalid_group_400Exception() {

			// given
			User user = userRepository.save(UserFactory.createEmailLoginUser());

			// then
			assertThatCode(() -> userService.participateToGroup(user.getUserSeq(), 10L))
				.isInstanceOf(GlobalBadRequestException.class)
				.hasMessage("그룹이 존재하지 않습니다.");
		}

		@DisplayName("그룹이 존재한다면 참여 가능하다.")
		@Test
		void participate_valid_group() {

			// given
			User user = userRepository.save(UserFactory.createEmailLoginUser());
			UserGroup group = groupRepository.save(new UserGroup("테스트그룹", "invitationLink", GroupType.FRIEND, 1, 1L));

			// when
			userService.participateToGroup(user.getUserSeq(), group.getId());

			// then
			List<UserGroup> result = groupAndUserRepository.findUserGroupLoginUserAssociated(user.getUserSeq());
			assertThat(result).hasSize(1)
				.extracting("groupName")
				.containsExactlyInAnyOrder("테스트그룹");
		}
	}

	@DisplayName("사용자 식별자로 사용자 정보를 가지고 올때")
	@Nested
	class When_get_user_info_by_identifier {

		@DisplayName("식별자에 해당하는 사용자가 DB에 존재하면 닉네임을 조회할 수 있다.")
		@Test
		void get_user_info_by_user_identifier() {
			// given
			User user = userRepository.save(UserFactory.createEmailLoginUser());

			// when
			String nickname = userService.findNicknameByUserIdentifier(user.getUserSeq());

			// then
			assertThat(nickname).isEqualTo(user.getNickname());
		}

		@DisplayName("DB에 유저가 없다면 예외를 반환한다. - 400 예외")
		@Test
		void get_invalid_user_info() {
			// given
			userRepository.save(UserFactory.createEmailLoginUser());

			// then
			assertThatCode(() -> userService.findNicknameByUserIdentifier(0L))
				.isInstanceOf(GlobalBadRequestException.class)
				.hasMessage("해당 id의 유저가 없습니다.");
		}
	}

	@DisplayName("사용자가 작성한 메모리 리스트를 조회할 때")
	@Nested
	class When_get_memories_login_user_write {
		@DisplayName("그룹 필터링을 하지 않으면 모든 데이터가 조회된다.")
		@Test
		void get_memories_login_user_write_with_no_group_filter() {
			// given
			User user = userRepository.save(UserFactory.createEmailLoginUser());

			Memory memory1 = Memory.builder().userId(user.getUserSeq()).title("테스트 제목").content("테스트 컨텐츠")
				.groupInfo(new GroupInfo("테스트 그룹", GroupType.FRIEND, 1L))
				.openType(OpenType.ALL).stars(4L).visitedDate(LocalDateTime.of(2023, 5, 29, 10, 20))
				.build();

			memory1.setMemoryImages(List.of(new MemoryImage("testImage")));
			memory1.setKeyword(List.of("기뻐요", "좋아요"));

			Memory memory2 = Memory.builder().userId(user.getUserSeq()).title("테스트 제목2").content("테스트 컨텐츠2")
				.groupInfo(new GroupInfo("테스트 그룹", GroupType.FRIEND, 1L))
				.openType(OpenType.ALL).stars(4L).visitedDate(LocalDateTime.of(2023, 5, 29, 10, 20))
				.build();

			memory2.setMemoryImages(List.of(new MemoryImage("testImage")));
			memory2.setKeyword(List.of("기뻐요", "좋아요"));

			memoryRepository.saveAll(List.of(memory1, memory2));

			// when
			Slice<MemoryResponseDto> result = userService.findMemoriesLoginUserWrite(PageRequest.of(0, 10),
				user.getUserSeq(), null);

			// then
			assertThat(result.getContent()).hasSize(2)
				.extracting("title", "content", "loginUserWrite", "keyword")
				.containsExactlyInAnyOrder(
					tuple("테스트 제목", "테스트 컨텐츠", true, List.of("기뻐요", "좋아요")),
					tuple("테스트 제목2", "테스트 컨텐츠2", true, List.of("기뻐요", "좋아요")));
		}

		@DisplayName("그룹 필터링을 하면 해당 그룹의 메모리 리스트만 조회된다.")
		@Test
		void get_memories_login_user_write_with_group_filter() {
			// given
			User user = userRepository.save(UserFactory.createEmailLoginUser());

			Memory memory1 = Memory.builder().userId(user.getUserSeq()).title("테스트 제목").content("테스트 컨텐츠")
				.groupInfo(new GroupInfo("테스트 그룹", GroupType.FRIEND, 1L))
				.openType(OpenType.ALL).stars(4L).visitedDate(LocalDateTime.of(2023, 5, 29, 10, 20))
				.build();

			memory1.setMemoryImages(List.of(new MemoryImage("testImage")));
			memory1.setKeyword(List.of("기뻐요", "좋아요"));

			Memory memory2 = Memory.builder().userId(user.getUserSeq()).title("테스트 제목2").content("테스트 컨텐츠2")
				.groupInfo(new GroupInfo("테스트 그룹", GroupType.FAMILY, 1L))
				.openType(OpenType.ALL).stars(4L).visitedDate(LocalDateTime.of(2023, 5, 29, 10, 20))
				.build();

			memory2.setMemoryImages(List.of(new MemoryImage("testImage")));
			memory2.setKeyword(List.of("기뻐요", "좋아요"));

			memoryRepository.saveAll(List.of(memory1, memory2));

			// when
			Slice<MemoryResponseDto> result = userService.findMemoriesLoginUserWrite(PageRequest.of(0, 10),
				user.getUserSeq(), GroupType.FRIEND);

			// then
			assertThat(result.getContent()).hasSize(1)
				.extracting("title", "content", "loginUserWrite", "keyword")
				.containsExactlyInAnyOrder(tuple("테스트 제목", "테스트 컨텐츠", true, List.of("기뻐요", "좋아요")));

		}

	}

	@DisplayName("사용자와 같은 그룹에 속한 구성원들이 남긴 메모리 리스트를 조회할 때")
	@Nested
	class When_get_memories_belong_to_my_group_write {

		@DisplayName("메모리의 공개 범위가 전체나 그룹 공개면 모두 다 조회 가능하다.")
		@Test
		void get_memories_belong_to_my_group_write() {
			// given
			User user = userRepository.save(UserFactory.createEmailLoginUser());
			User user2 = userRepository.save(UserFactory.createEmailLoginUser());
			User user3 = userRepository.save(UserFactory.createEmailLoginUser());

			Memory memory1 = Memory.builder().userId(user.getUserSeq()).title("테스트 제목").content("테스트 컨텐츠")
				.groupInfo(new GroupInfo("테스트 그룹", GroupType.FRIEND, 1L))
				.openType(OpenType.GROUP).stars(4L).visitedDate(LocalDateTime.of(2023, 5, 29, 10, 20))
				.build();
			memory1.setMemoryImages(List.of(new MemoryImage("testImage")));
			memory1.setKeyword(List.of("기뻐요", "좋아요"));

			Memory memory2 = Memory.builder().userId(user.getUserSeq()).title("테스트 제목2").content("테스트 컨텐츠2")
				.groupInfo(new GroupInfo("테스트 그룹", GroupType.FAMILY, 1L))
				.openType(OpenType.ALL).stars(4L).visitedDate(LocalDateTime.of(2023, 5, 29, 10, 20))
				.build();
			memory2.setMemoryImages(List.of(new MemoryImage("testImage")));
			memory2.setKeyword(List.of("기뻐요", "좋아요"));

			Memory memory3 = Memory.builder().userId(user2.getUserSeq()).title("테스트 제목").content("테스트 컨텐츠")
				.groupInfo(new GroupInfo("테스트 그룹", GroupType.FRIEND, 1L))
				.openType(OpenType.ALL).stars(4L).visitedDate(LocalDateTime.of(2023, 5, 29, 10, 20))
				.build();
			memory3.setMemoryImages(List.of(new MemoryImage("testImage")));
			memory3.setKeyword(List.of("기뻐요", "좋아요"));

			Memory memory4 = Memory.builder().userId(user2.getUserSeq()).title("테스트 제목2").content("테스트 컨텐츠2")
				.groupInfo(new GroupInfo("테스트 그룹", GroupType.FAMILY, 1L))
				.openType(OpenType.ALL).stars(4L).visitedDate(LocalDateTime.of(2023, 5, 29, 10, 20))
				.build();
			memory4.setMemoryImages(List.of(new MemoryImage("testImage")));
			memory4.setKeyword(List.of("기뻐요", "좋아요"));

			Memory memory5 = Memory.builder().userId(user3.getUserSeq()).title("테스트 제목").content("테스트 컨텐츠")
				.groupInfo(new GroupInfo("테스트 그룹", GroupType.FRIEND, 1L))
				.openType(OpenType.GROUP).stars(4L).visitedDate(LocalDateTime.of(2023, 5, 29, 10, 20))
				.build();
			memory5.setMemoryImages(List.of(new MemoryImage("testImage")));
			memory5.setKeyword(List.of("기뻐요", "좋아요"));

			Memory memory6 = Memory.builder().userId(user3.getUserSeq()).title("테스트 제목2").content("테스트 컨텐츠2")
				.groupInfo(new GroupInfo("테스트 그룹", GroupType.FAMILY, 1L))
				.openType(OpenType.ALL).stars(4L).visitedDate(LocalDateTime.of(2023, 5, 29, 10, 20))
				.build();
			memory6.setMemoryImages(List.of(new MemoryImage("testImage")));
			memory6.setKeyword(List.of("기뻐요", "좋아요"));

			memoryRepository.saveAll(List.of(memory1, memory2, memory3, memory4, memory5, memory6));

			UserGroup group = groupRepository.save(new UserGroup("테스트그룹1", "invitationLink", GroupType.FRIEND, 1, 1L));

			userService.participateToGroup(user.getUserSeq(), group.getId());
			userService.participateToGroup(user2.getUserSeq(), group.getId());
			userService.participateToGroup(user3.getUserSeq(), group.getId());

			// when
			Slice<MemoryResponseDto> result = userService.findMemoriesUsersBelongToMyGroupWrite(PageRequest.of(0, 10),
				group.getId(), user.getUserSeq());

			// then
			assertThat(result.getContent()).hasSize(6);
		}

		@DisplayName("메모리의 공개 범위가 개인이라면 조회 불가능하다.")
		@Test
		void get_memories_belong_to_my_group_write_private() {
			// given
			User user = userRepository.save(UserFactory.createEmailLoginUser());
			User user2 = userRepository.save(UserFactory.createEmailLoginUser());
			User user3 = userRepository.save(UserFactory.createEmailLoginUser());

			Memory memory1 = Memory.builder().userId(user.getUserSeq()).title("테스트 제목").content("테스트 컨텐츠")
				.groupInfo(new GroupInfo("테스트 그룹", GroupType.FRIEND, 1L))
				.openType(OpenType.PRIVATE).stars(4L).visitedDate(LocalDateTime.of(2023, 5, 29, 10, 20))
				.build();
			memory1.setMemoryImages(List.of(new MemoryImage("testImage")));
			memory1.setKeyword(List.of("기뻐요", "좋아요"));

			Memory memory2 = Memory.builder().userId(user.getUserSeq()).title("테스트 제목2").content("테스트 컨텐츠2")
				.groupInfo(new GroupInfo("테스트 그룹", GroupType.FAMILY, 1L))
				.openType(OpenType.ALL).stars(4L).visitedDate(LocalDateTime.of(2023, 5, 29, 10, 20))
				.build();
			memory2.setMemoryImages(List.of(new MemoryImage("testImage")));
			memory2.setKeyword(List.of("기뻐요", "좋아요"));

			Memory memory3 = Memory.builder().userId(user2.getUserSeq()).title("테스트 제목").content("테스트 컨텐츠")
				.groupInfo(new GroupInfo("테스트 그룹", GroupType.FRIEND, 1L))
				.openType(OpenType.ALL).stars(4L).visitedDate(LocalDateTime.of(2023, 5, 29, 10, 20))
				.build();
			memory3.setMemoryImages(List.of(new MemoryImage("testImage")));
			memory3.setKeyword(List.of("기뻐요", "좋아요"));

			Memory memory4 = Memory.builder().userId(user2.getUserSeq()).title("테스트 제목2").content("테스트 컨텐츠2")
				.groupInfo(new GroupInfo("테스트 그룹", GroupType.FAMILY, 1L))
				.openType(OpenType.PRIVATE).stars(4L).visitedDate(LocalDateTime.of(2023, 5, 29, 10, 20))
				.build();
			memory4.setMemoryImages(List.of(new MemoryImage("testImage")));
			memory4.setKeyword(List.of("기뻐요", "좋아요"));

			Memory memory5 = Memory.builder().userId(user3.getUserSeq()).title("테스트 제목").content("테스트 컨텐츠")
				.groupInfo(new GroupInfo("테스트 그룹", GroupType.FRIEND, 1L))
				.openType(OpenType.GROUP).stars(4L).visitedDate(LocalDateTime.of(2023, 5, 29, 10, 20))
				.build();
			memory5.setMemoryImages(List.of(new MemoryImage("testImage")));
			memory5.setKeyword(List.of("기뻐요", "좋아요"));

			Memory memory6 = Memory.builder().userId(user3.getUserSeq()).title("테스트 제목2").content("테스트 컨텐츠2")
				.groupInfo(new GroupInfo("테스트 그룹", GroupType.FAMILY, 1L))
				.openType(OpenType.ALL).stars(4L).visitedDate(LocalDateTime.of(2023, 5, 29, 10, 20))
				.build();
			memory6.setMemoryImages(List.of(new MemoryImage("testImage")));
			memory6.setKeyword(List.of("기뻐요", "좋아요"));

			memoryRepository.saveAll(List.of(memory1, memory2, memory3, memory4, memory5, memory6));

			UserGroup group = groupRepository.save(new UserGroup("테스트그룹1", "invitationLink", GroupType.FRIEND, 1, 1L));

			userService.participateToGroup(user.getUserSeq(), group.getId());
			userService.participateToGroup(user2.getUserSeq(), group.getId());
			userService.participateToGroup(user3.getUserSeq(), group.getId());

			// when
			Slice<MemoryResponseDto> result = userService.findMemoriesUsersBelongToMyGroupWrite(PageRequest.of(0, 10),
				group.getId(), user.getUserSeq());

			// then
			assertThat(result.getContent()).hasSize(4);
		}
	}

}


