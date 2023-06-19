package cmc.mellyserver.mellydomain.unit.comment.repository;

import cmc.mellyserver.mellydomain.comment.domain.Comment;
import cmc.mellyserver.mellydomain.comment.domain.repository.CommentQueryRepository;
import cmc.mellyserver.mellydomain.comment.domain.repository.CommentRepository;
import cmc.mellyserver.mellydomain.common.enums.GroupType;
import cmc.mellyserver.mellydomain.common.enums.OpenType;
import cmc.mellyserver.mellydomain.memory.domain.Memory;
import cmc.mellyserver.mellydomain.memory.domain.repository.MemoryRepository;
import cmc.mellyserver.mellydomain.memory.domain.vo.GroupInfo;
import cmc.mellyserver.mellydomain.unit.RepositoryTest;
import cmc.mellyserver.mellydomain.user.domain.User;
import cmc.mellyserver.mellydomain.user.domain.repository.UserRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

import static cmc.mellyserver.mellydomain.unit.common.fixtures.UserFixtures.제민;

public class CommentQueryRepositoryTest extends RepositoryTest {


    @Autowired
    private MemoryRepository memoryRepository;

    @Autowired
    private CommentQueryRepository commentQueryRepository;

    @Autowired
    private CommentRepository commentRepository;

    @Autowired
    private UserRepository userRepository;

//    public List<Comment> findComment(Long memoryId) {
//
//        return query.selectFrom(comment)
//                .leftJoin(comment.parent)
//                .fetchJoin()
//                .where(
//                        comment.memoryId.eq(memoryId),
//                )
//                .orderBy(
//                        comment.parent.id.asc().nullsFirst(),
//                        comment.createdDate.asc()
//                ).fetch();
//
//    }

    @DisplayName("메모리에 존재하는 댓글을 모두 가지고 올 수 있다.")
    @Test
    void 메모리에_존재하는_댓글_모두_조회() {
        // given
        User 제민 = userRepository.save(제민());
        Memory memory_제민 = Memory.builder().title("테스트 메모리").userId(제민.getUserSeq())
                .content("테스트 본문")
                .groupInfo(new GroupInfo("친구", GroupType.FRIEND, null))
                .openType(OpenType.ALL).build();

        memory_제민.setKeyword(List.of("좋아요"));
        Memory savedMemory = memoryRepository.save(memory_제민);
        Comment comment = Comment.createComment("테스트 컨텐츠", 제민.getUserSeq(), savedMemory.getId(),
                null, 3L);
        commentRepository.save(comment);

        // when
        List<Comment> commentList = commentQueryRepository.findComment(savedMemory.getId());

        // then
        Assertions.assertThat(commentList).hasSize(1);
    }
}
