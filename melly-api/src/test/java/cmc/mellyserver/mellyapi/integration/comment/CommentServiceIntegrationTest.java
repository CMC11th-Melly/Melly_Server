package cmc.mellyserver.mellyapi.integration.comment;

import cmc.mellyserver.mellyapi.common.factory.UserFactory;
import cmc.mellyserver.mellyapi.integration.IntegrationTest;
import cmc.mellyserver.mellycommon.codes.ErrorCode;
import cmc.mellyserver.mellycommon.enums.AgeGroup;
import cmc.mellyserver.mellycommon.enums.DeleteStatus;
import cmc.mellyserver.mellycore.comment.application.dto.request.CommentRequestDto;
import cmc.mellyserver.mellycore.comment.domain.Comment;
import cmc.mellyserver.mellycore.comment.domain.CommentLike;
import cmc.mellyserver.mellycore.memory.application.dto.request.CreateMemoryRequestDto;
import cmc.mellyserver.mellycore.memory.domain.Memory;
import cmc.mellyserver.mellycore.user.domain.User;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

public class CommentServiceIntegrationTest extends IntegrationTest {

    @DisplayName("제일 상위의 부모 댓글을 생성할 수 있다.")
    @Test
    void create_comment() {

        // given
        User user = userRepository.save(UserFactory.createEmailLoginUser());
        User mentionedUser = userRepository.save(User.builder().nickname("멘션된 유저").ageGroup(AgeGroup.TWO).build());

        CreateMemoryRequestDto createMemoryRequestDto = CreateMemoryRequestDto.builder()
                .id(user.getId()).title("테스트 제목")
                .content("테스트 컨텐츠").placeName("테스트 장소")
                .placeCategory("카페").lat(1.234).lng(1.234)
                .build();

        Memory memory = memoryService.createMemory(createMemoryRequestDto);

        CommentRequestDto commentRequestDto = CommentRequestDto.builder()
                .content("테스트 댓글")
                .userId(user.getId())
                .mentionUserId(mentionedUser.getId())
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
                .id(user.getId()).title("테스트 제목")
                .content("테스트 컨텐츠").placeName("테스트 장소")
                .placeCategory("카페").lat(1.234).lng(1.234)
                .build();

        Memory memory = memoryService.createMemory(createMemoryRequestDto);

        CommentRequestDto commentRequestDto = CommentRequestDto.builder()
                .content("테스트 댓글")
                .userId(user.getId())
                .mentionUserId(mentionedUser.getId())
                .memoryId(memory.getId())
                .parentId(2L)
                .build();

        // when then
        assertThatThrownBy(() -> commentService.saveComment(commentRequestDto))
                .isInstanceOf(GlobalBadRequestException.class)
                .hasMessage(ErrorCode.NO_SUCH_COMMENT.getMessage());

    }

    @DisplayName("댓글을 달아야하는 메모리를 찾지 못하면 예외가 발생한다.")
    @Test
    void create_comment_not_found_memory_exception() {

        // given
        User user = userRepository.save(UserFactory.createEmailLoginUser());
        User mentionedUser = userRepository.save(User.builder().nickname("멘션된 유저").ageGroup(AgeGroup.TWO).build());

        CreateMemoryRequestDto createMemoryRequestDto = CreateMemoryRequestDto.builder()
                .id(user.getId()).title("테스트 제목")
                .content("테스트 컨텐츠").placeName("테스트 장소")
                .placeCategory("카페").lat(1.234).lng(1.234)
                .build();

        Memory memory = memoryService.createMemory(createMemoryRequestDto);

        CommentRequestDto commentRequestDto = CommentRequestDto.builder()
                .content("테스트 댓글")
                .userId(user.getId())
                .mentionUserId(mentionedUser.getId())
                .memoryId(2L)
                .parentId(2L)
                .build();

        // when then
        assertThatThrownBy(() -> commentService.saveComment(commentRequestDto))
                .isInstanceOf(GlobalBadRequestException.class)
                .hasMessage(ErrorCode.NO_SUCH_MEMORY.getMessage());

    }

    @DisplayName("댓글을 수정할 수 있다.")
    @Test
    void update_comment() {

        // given
        User user = userRepository.save(UserFactory.createEmailLoginUser());
        User mentionedUser = userRepository.save(User.builder().nickname("멘션된 유저").ageGroup(AgeGroup.TWO).build());

        CreateMemoryRequestDto createMemoryRequestDto = CreateMemoryRequestDto.builder()
                .id(user.getId()).title("테스트 제목")
                .content("테스트 컨텐츠").placeName("테스트 장소")
                .placeCategory("카페").lat(1.234).lng(1.234)
                .build();

        Memory memory = memoryService.createMemory(createMemoryRequestDto);

        CommentRequestDto commentRequestDto = CommentRequestDto.builder()
                .content("테스트 댓글")
                .userId(user.getId())
                .mentionUserId(mentionedUser.getId())
                .memoryId(memory.getId())
                .parentId(null)
                .build();

        Comment comment = commentService.saveComment(commentRequestDto);

        // when
        Comment updatedComment = commentService.updateComment(comment.getId(), "수정된 내용");

        // then
        assertThat(updatedComment.getContent()).isEqualTo("수정된 내용");
    }

    @DisplayName("댓글을 수정할때 해당 댓글이 DB에 없으면 예외를 반환한다.")
    @Test
    void update_comment_not_exist_comment_exception() {

        // given
        User user = userRepository.save(UserFactory.createEmailLoginUser());
        User mentionedUser = userRepository.save(User.builder().nickname("멘션된 유저").ageGroup(AgeGroup.TWO).build());

        CreateMemoryRequestDto createMemoryRequestDto = CreateMemoryRequestDto.builder()
                .id(user.getId()).title("테스트 제목")
                .content("테스트 컨텐츠").placeName("테스트 장소")
                .placeCategory("카페").lat(1.234).lng(1.234)
                .build();

        Memory memory = memoryService.createMemory(createMemoryRequestDto);

        CommentRequestDto commentRequestDto = CommentRequestDto.builder()
                .content("테스트 댓글")
                .userId(user.getId())
                .mentionUserId(mentionedUser.getId())
                .memoryId(memory.getId())
                .parentId(null)
                .build();

        Comment comment = commentService.saveComment(commentRequestDto);

        // when then
        assertThatThrownBy(() -> commentService.updateComment(-1L, "수정된 내용"))
                .isInstanceOf(GlobalBadRequestException.class)
                .hasMessage(ErrorCode.NO_SUCH_COMMENT.getMessage());
    }

    @DisplayName("자식이 없는 댓글은 완전 삭제를 한다.")
    @Test
    void remove_comment() {

        // given
        User user = userRepository.save(UserFactory.createEmailLoginUser());
        User mentionedUser = userRepository.save(User.builder().nickname("멘션된 유저").ageGroup(AgeGroup.TWO).build());

        CreateMemoryRequestDto createMemoryRequestDto = CreateMemoryRequestDto.builder()
                .id(user.getId()).title("테스트 제목")
                .content("테스트 컨텐츠").placeName("테스트 장소")
                .placeCategory("카페").lat(1.234).lng(1.234)
                .build();

        Memory memory = memoryService.createMemory(createMemoryRequestDto);

        CommentRequestDto commentRequestDto = CommentRequestDto.builder()
                .content("테스트 댓글")
                .userId(user.getId())
                .mentionUserId(mentionedUser.getId())
                .memoryId(memory.getId())
                .parentId(null)
                .build();

        Comment comment = commentService.saveComment(commentRequestDto);

        // when
        commentService.deleteComment(comment.getId());

        // then
        Optional<Comment> deleted = commentRepository.findById(comment.getId());
        assertThat(deleted).isEmpty();
    }

    @DisplayName("자식이 있는 부모 댓글은 삭제 처리만 한다.")
    @Test
    void remove_comment_with_child() {

        // given
        User user = userRepository.save(UserFactory.createEmailLoginUser());
        User mentionedUser = userRepository.save(User.builder().nickname("멘션된 유저").ageGroup(AgeGroup.TWO).build());

        CreateMemoryRequestDto createMemoryRequestDto = CreateMemoryRequestDto.builder()
                .id(user.getId()).title("테스트 제목")
                .content("테스트 컨텐츠").placeName("테스트 장소")
                .placeCategory("카페").lat(1.234).lng(1.234)
                .build();

        Memory memory = memoryService.createMemory(createMemoryRequestDto);

        CommentRequestDto commentRequestDto = CommentRequestDto.builder()
                .content("테스트 댓글")
                .userId(user.getId())
                .mentionUserId(mentionedUser.getId())
                .memoryId(memory.getId())
                .parentId(null)
                .build();

        Comment comment = commentService.saveComment(commentRequestDto);

        CommentRequestDto commentRequestDto2 = CommentRequestDto.builder()
                .content("테스트 댓글")
                .userId(user.getId())
                .mentionUserId(mentionedUser.getId())
                .memoryId(memory.getId())
                .parentId(comment.getId())
                .build();

        Comment comment2 = commentService.saveComment(commentRequestDto2);

        // when
        commentService.deleteComment(comment.getId());

        // then
        Comment deletedComment = commentRepository.findById(comment.getId()).get();
        assertThat(deletedComment.getIsDeleted()).isEqualTo(DeleteStatus.Y);
    }

    @DisplayName("댓글에 좋아요를 취소한다.")
    @Test
    void cancel_comment_like() {

        // given
        User user = userRepository.save(UserFactory.createEmailLoginUser());
        User mentionedUser = userRepository.save(User.builder().nickname("멘션된 유저").ageGroup(AgeGroup.TWO).build());

        CreateMemoryRequestDto createMemoryRequestDto = CreateMemoryRequestDto.builder()
                .id(user.getId()).title("테스트 제목")
                .content("테스트 컨텐츠").placeName("테스트 장소")
                .placeCategory("카페").lat(1.234).lng(1.234)
                .build();

        Memory memory = memoryService.createMemory(createMemoryRequestDto);

        CommentRequestDto commentRequestDto = CommentRequestDto.builder()
                .content("테스트 댓글")
                .userId(user.getId())
                .mentionUserId(mentionedUser.getId())
                .memoryId(memory.getId())
                .parentId(null)
                .build();

        Comment comment = commentService.saveComment(commentRequestDto);
        commentService.saveCommentLike(user.getId(), comment.getId());
        // when
        commentService.deleteCommentLike(user.getId(), comment.getId());

        // then
        Optional<CommentLike> result = commentLikeRepository.findCommentLikeByCommentIdAndUserId(
                comment.getId(), user.getId());

        assertThat(result).isEmpty();
    }

    @DisplayName("댓글의 좋아요를 취소할때 좋아요 정보가 DB에 존재하지 않는다면 예외가 발생한다.")
    @Test
    void cancel_comment_like_not_exist_commentLike() {

        // given
        User user = userRepository.save(UserFactory.createEmailLoginUser());
        User mentionedUser = userRepository.save(User.builder().nickname("멘션된 유저").ageGroup(AgeGroup.TWO).build());

        CreateMemoryRequestDto createMemoryRequestDto = CreateMemoryRequestDto.builder()
                .id(user.getId()).title("테스트 제목")
                .content("테스트 컨텐츠").placeName("테스트 장소")
                .placeCategory("카페").lat(1.234).lng(1.234)
                .build();

        Memory memory = memoryService.createMemory(createMemoryRequestDto);

        CommentRequestDto commentRequestDto = CommentRequestDto.builder()
                .content("테스트 댓글")
                .userId(user.getId())
                .mentionUserId(mentionedUser.getId())
                .memoryId(memory.getId())
                .parentId(null)
                .build();

        Comment comment = commentService.saveComment(commentRequestDto);

        // when then
        assertThatThrownBy(() -> commentService.deleteCommentLike(user.getId(), comment.getId()))
                .isInstanceOf(GlobalBadRequestException.class)
                .hasMessage(ErrorCode.NO_SUCH_COMMENT_LIKE.getMessage());
    }

    @DisplayName("댓글에 좋아요를 단다.")
    @Test
    void add_comment_like() {

        // given
        User user = userRepository.save(UserFactory.createEmailLoginUser());
        User mentionedUser = userRepository.save(User.builder().nickname("멘션된 유저").ageGroup(AgeGroup.TWO).build());

        CreateMemoryRequestDto createMemoryRequestDto = CreateMemoryRequestDto.builder()
                .id(user.getId()).title("테스트 제목")
                .content("테스트 컨텐츠").placeName("테스트 장소")
                .placeCategory("카페").lat(1.234).lng(1.234)
                .build();

        Memory memory = memoryService.createMemory(createMemoryRequestDto);

        CommentRequestDto commentRequestDto = CommentRequestDto.builder()
                .content("테스트 댓글")
                .userId(user.getId())
                .mentionUserId(mentionedUser.getId())
                .memoryId(memory.getId())
                .parentId(null)
                .build();

        Comment comment = commentService.saveComment(commentRequestDto);

        // when
        CommentLike commentLike = commentService.saveCommentLike(user.getId(), comment.getId());

        // then
        assertThat(commentLike.getComment().getId()).isEqualTo(comment.getId());
        assertThat(commentLike.getSocialId()).isEqualTo(user.getId());
    }

    @DisplayName("댓글에 좋아요를 달려고 할때 이미 좋아요를 한 적이 있다면 중복 예외가 발생한다.")
    @Test
    void add_comment_like_duplicated_exception() {

        // given
        User user = userRepository.save(UserFactory.createEmailLoginUser());
        User mentionedUser = userRepository.save(User.builder().nickname("멘션된 유저").ageGroup(AgeGroup.TWO).build());

        CreateMemoryRequestDto createMemoryRequestDto = CreateMemoryRequestDto.builder()
                .id(user.getId()).title("테스트 제목")
                .content("테스트 컨텐츠").placeName("테스트 장소")
                .placeCategory("카페").lat(1.234).lng(1.234)
                .build();

        Memory memory = memoryService.createMemory(createMemoryRequestDto);

        CommentRequestDto commentRequestDto = CommentRequestDto.builder()
                .content("테스트 댓글")
                .userId(user.getId())
                .mentionUserId(mentionedUser.getId())
                .memoryId(memory.getId())
                .parentId(null)
                .build();

        Comment comment = commentService.saveComment(commentRequestDto);

        // when
        commentService.saveCommentLike(user.getId(), comment.getId());

        // then
        assertThatThrownBy(() -> commentService.saveCommentLike(user.getId(), comment.getId()))
                .isInstanceOf(GlobalBadRequestException.class)
                .hasMessage(ErrorCode.DUPLICATED_COMMENT_LIKE.getMessage());
    }

}
