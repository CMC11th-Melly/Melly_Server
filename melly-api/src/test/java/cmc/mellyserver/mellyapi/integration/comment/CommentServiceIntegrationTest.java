package cmc.mellyserver.mellyapi.integration.comment;

import static org.assertj.core.api.Assertions.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import cmc.mellyserver.mellyapi.comment.application.dto.request.CommentRequestDto;
import cmc.mellyserver.mellyapi.common.exception.ExceptionCodeAndDetails;
import cmc.mellyserver.mellyapi.common.exception.GlobalBadRequestException;
import cmc.mellyserver.mellyapi.common.factory.UserFactory;
import cmc.mellyserver.mellyapi.integration.IntegrationTest;
import cmc.mellyserver.mellyapi.memory.application.dto.request.CreateMemoryRequestDto;
import cmc.mellyserver.mellycore.comment.domain.Comment;
import cmc.mellyserver.mellycore.common.enums.AgeGroup;
import cmc.mellyserver.mellycore.memory.domain.Memory;
import cmc.mellyserver.mellycore.user.domain.User;

public class CommentServiceIntegrationTest extends IntegrationTest {

	@DisplayName("제일 상위의 부모 댓글을 생성할 수 있다.")
	@Test
	void create_comment() {

		// given
		User user = userRepository.save(UserFactory.createEmailLoginUser());
		User mentionedUser = userRepository.save(User.builder().nickname("멘션된 유저").ageGroup(AgeGroup.TWO).build());

		CreateMemoryRequestDto createMemoryRequestDto = CreateMemoryRequestDto.builder()
			.userSeq(user.getUserSeq()).title("테스트 제목")
			.content("테스트 컨텐츠").placeName("테스트 장소")
			.placeCategory("카페").lat(1.234).lng(1.234)
			.build();

		Memory memory = memoryService.createMemory(createMemoryRequestDto);

		CommentRequestDto commentRequestDto = CommentRequestDto.builder()
			.content("테스트 댓글")
			.userId(user.getUserSeq())
			.mentionUserId(mentionedUser.getUserSeq())
			.memoryId(memory.getId())
			.parentId(null)
			.build();

		// when
		Comment comment = commentService.saveComment(commentRequestDto);

		// then
		assertThat(comment.getContent()).isEqualTo("테스트 댓글");
	}

	@DisplayName("자식 댓글을 추가할때 상위 댓글을 DB에서 찾지 못하면 예외가 발생한다.")
	@Test
	void create_comment_not_found_parent_comment_exception() {

		// given
		User user = userRepository.save(UserFactory.createEmailLoginUser());
		User mentionedUser = userRepository.save(User.builder().nickname("멘션된 유저").ageGroup(AgeGroup.TWO).build());

		CreateMemoryRequestDto createMemoryRequestDto = CreateMemoryRequestDto.builder()
			.userSeq(user.getUserSeq()).title("테스트 제목")
			.content("테스트 컨텐츠").placeName("테스트 장소")
			.placeCategory("카페").lat(1.234).lng(1.234)
			.build();

		Memory memory = memoryService.createMemory(createMemoryRequestDto);

		CommentRequestDto commentRequestDto = CommentRequestDto.builder()
			.content("테스트 댓글")
			.userId(user.getUserSeq())
			.mentionUserId(mentionedUser.getUserSeq())
			.memoryId(memory.getId())
			.parentId(2L)
			.build();

		// when then
		assertThatThrownBy(() -> commentService.saveComment(commentRequestDto))
			.isInstanceOf(GlobalBadRequestException.class)
			.hasMessage(ExceptionCodeAndDetails.NO_SUCH_COMMENT.getMessage());

	}

	@DisplayName("댓글을 달아야하는 메모리를 찾지 못하면 예외가 발생한다.")
	@Test
	void create_comment_not_found_memory_exception() {

		// given
		User user = userRepository.save(UserFactory.createEmailLoginUser());
		User mentionedUser = userRepository.save(User.builder().nickname("멘션된 유저").ageGroup(AgeGroup.TWO).build());

		CreateMemoryRequestDto createMemoryRequestDto = CreateMemoryRequestDto.builder()
			.userSeq(user.getUserSeq()).title("테스트 제목")
			.content("테스트 컨텐츠").placeName("테스트 장소")
			.placeCategory("카페").lat(1.234).lng(1.234)
			.build();

		Memory memory = memoryService.createMemory(createMemoryRequestDto);

		CommentRequestDto commentRequestDto = CommentRequestDto.builder()
			.content("테스트 댓글")
			.userId(user.getUserSeq())
			.mentionUserId(mentionedUser.getUserSeq())
			.memoryId(2L)
			.parentId(2L)
			.build();

		// when then
		assertThatThrownBy(() -> commentService.saveComment(commentRequestDto))
			.isInstanceOf(GlobalBadRequestException.class)
			.hasMessage(ExceptionCodeAndDetails.NO_SUCH_MEMORY.getMessage());

	}
}
