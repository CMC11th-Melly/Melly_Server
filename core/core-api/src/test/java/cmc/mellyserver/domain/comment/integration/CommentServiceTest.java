package cmc.mellyserver.domain.comment.integration;

import static org.assertj.core.api.Assertions.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import cmc.mellyserver.dbcore.comment.comment.Comment;
import cmc.mellyserver.dbcore.comment.comment.CommentRepository;
import cmc.mellyserver.dbcore.memory.memory.Memory;
import cmc.mellyserver.dbcore.memory.memory.MemoryRepository;
import cmc.mellyserver.dbcore.memory.memory.OpenType;
import cmc.mellyserver.dbcore.place.Place;
import cmc.mellyserver.dbcore.place.PlaceRepository;
import cmc.mellyserver.dbcore.user.User;
import cmc.mellyserver.dbcore.user.UserRepository;
import cmc.mellyserver.domain.comment.CommentService;
import cmc.mellyserver.domain.comment.dto.request.CommentRequestDto;
import cmc.mellyserver.support.IntegrationTestSupport;
import fixtures.MemoryFixtures;
import fixtures.PlaceFixtures;
import fixtures.UserFixtures;

public class CommentServiceTest extends IntegrationTestSupport {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private MemoryRepository memoryRepository;

    @Autowired
    private PlaceRepository placeRepository;

    @Autowired
    private CommentRepository commentRepository;

    @Autowired
    private CommentService commentService;

    @DisplayName("제일 상위의 부모 댓글을 생성할 수 있다")
    @Test
    void 제일_상위의_부모_댓글을_생성한다() {

        // given
        User 모카 = userRepository.save(UserFixtures.모카());
        Place 스타벅스 = placeRepository.save(PlaceFixtures.스타벅스());
        Memory 메모리 = memoryRepository.save(MemoryFixtures.메모리(스타벅스.getId(), 모카.getId(), null, "테스트 메모리", OpenType.ALL));

        // when
        Comment comment = commentService.saveComment(CommentRequestDto.builder()
            .content("테스트 댓글")
            .memoryId(메모리.getId())
            .rootId(null)
            .mentionId(null)
            .userId(모카.getId())
            .build());

        // then
        assertThat(comment.getContent()).isEqualTo("테스트 댓글");
    }

    // @DisplayName("댓글을 수정할 수 있다.")
    // @Test
    // void 댓글을_수정할_수_있다() {
    //
    //     // given
    //     User 모카 = userRepository.save(UserFixtures.모카());
    //     Place 스타벅스 = placeRepository.save(PlaceFixtures.스타벅스());
    //     Memory 메모리 = memoryRepository.save(MemoryFixtures.메모리(스타벅스.getId(), 모카.getId(), null, "테스트 메모리", OpenType.ALL));
    //     Comment comment = commentRepository.save(Comment.createRoot("테스트 제목", 모카, 메모리.getId(), null));
    //
    //     // when
    //     commentService.updateComment(comment.getId(), "수정된 내용");
    //
    //     // then
    //     Comment 수정댓글 = commentRepository.findById(comment.getId()).get();
    //     assertThat(수정댓글.getContent()).isEqualTo("수정된 내용");
    // }
    //
    // @DisplayName("자식 댓글을 등록하면 부모 댓글과 함께 조회된다")
    // @Test
    // void 자식댓글을_등록하면_부모댓글과_함께_조회된다() {
    //
    //     // given
    //     User 모카 = userRepository.save(UserFixtures.모카());
    //     User 머식 = userRepository.save(UserFixtures.머식());
    //     User 금지 = userRepository.save(UserFixtures.금지());
    //     Place 스타벅스 = placeRepository.save(PlaceFixtures.스타벅스());
    //     Memory 메모리 = memoryRepository.save(MemoryFixtures.메모리(스타벅스.getId(), 모카.getId(), null, "테스트 메모리", OpenType.ALL));
    //     Comment 부모댓글 = commentRepository.save(Comment.createRoot("부모 댓글", 모카, 메모리.getId(), null));
    //     Comment 자식댓글1 = Comment.createChild("자식댓글_머식", 머식, 모카, 메모리.getId(), 부모댓글);
    //     Comment 자식댓글2 = Comment.createChild("자식댓글_금지", 금지, 머식, 메모리.getId(), 부모댓글);
    //     commentRepository.save(자식댓글1);
    //     commentRepository.save(자식댓글2);
    //
    //     // when
    //     CommentResponseDto comments = commentService.getComments(모카.getId(), 메모리.getId());
    //
    //     // then
    //     assertThat(comments.getCommentCount()).isEqualTo(3);
    //     assertThat(comments.getComments().get(0).getContent()).isEqualTo(부모댓글.getContent());
    //     assertThat(comments.getComments().get(0).getChildren().get(0).getContent()).isEqualTo(자식댓글1.getContent());
    //     assertThat(comments.getComments().get(0).getChildren().get(1).getContent()).isEqualTo(자식댓글2.getContent());
    // }
    //
    // @DisplayName("루트 댓글을 삭제하면 하위 댓글도 모두 삭제된다")
    // @Test
    // void 루트_댓글을_삭제하면_하위_댓글도_삭제된다() {
    //
    //     // given
    //     User 모카 = userRepository.save(UserFixtures.모카());
    //     User 머식 = userRepository.save(UserFixtures.머식());
    //     User 금지 = userRepository.save(UserFixtures.금지());
    //     Place 스타벅스 = placeRepository.save(PlaceFixtures.스타벅스());
    //     Memory 메모리 = memoryRepository.save(MemoryFixtures.메모리(스타벅스.getId(), 모카.getId(), null, "테스트 메모리", OpenType.ALL));
    //     Comment 부모댓글 = commentRepository.save(Comment.createRoot("부모 댓글", 모카, 메모리.getId(), null));
    //     Comment 자식댓글1 = Comment.createChild("자식댓글_머식", 머식, 모카, 메모리.getId(), 부모댓글);
    //     Comment 자식댓글2 = Comment.createChild("자식댓글_금지", 금지, 머식, 메모리.getId(), 부모댓글);
    //     commentRepository.save(자식댓글1);
    //     commentRepository.save(자식댓글2);
    //
    //     // when
    //     commentService.deleteComment(부모댓글.getId());
    //     CommentResponseDto comments = commentService.getComments(모카.getId(), 메모리.getId());
    //
    //     // then
    //     assertThat(comments.getCommentCount()).isEqualTo(0);
    // }
    //
    // @DisplayName("자식댓글만 삭제한다")
    // @Test
    // void 자식_댓글만_삭제한다() {
    //
    //     // given
    //     User 모카 = userRepository.save(UserFixtures.모카());
    //     User 머식 = userRepository.save(UserFixtures.머식());
    //     User 금지 = userRepository.save(UserFixtures.금지());
    //     Place 스타벅스 = placeRepository.save(PlaceFixtures.스타벅스());
    //     Memory 메모리 = memoryRepository.save(MemoryFixtures.메모리(스타벅스.getId(), 모카.getId(), null, "테스트 메모리", OpenType.ALL));
    //     Comment 부모댓글 = commentRepository.save(Comment.createRoot("부모 댓글", 모카, 메모리.getId(), null));
    //     Comment 자식댓글1 = Comment.createChild("자식댓글_머식", 머식, 모카, 메모리.getId(), 부모댓글);
    //     Comment 자식댓글2 = Comment.createChild("자식댓글_금지", 금지, 머식, 메모리.getId(), 부모댓글);
    //     commentRepository.save(자식댓글1);
    //     commentRepository.save(자식댓글2);
    //
    //     // when
    //     commentService.deleteComment(자식댓글2.getId());
    //     CommentResponseDto comments = commentService.getComments(모카.getId(), 메모리.getId());
    //
    //     // then
    //     assertThat(comments.getCommentCount()).isEqualTo(2);
    // }
}
