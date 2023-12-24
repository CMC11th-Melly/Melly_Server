package cmc.mellyserver.domain.comment;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cmc.mellyserver.common.aspect.lock.DistributedLock;
import cmc.mellyserver.common.aspect.lock.OptimisticLock;
import cmc.mellyserver.dbcore.comment.commenlike.CommentLike;
import cmc.mellyserver.dbcore.comment.comment.Comment;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CommentLikeService {

    private final CommentLikeReader commentLikeReader;

    private final CommentLikeWriter commentLikeWriter;

    private final CommentLikeValidator commentLikeValidator;

    private final CommentReader commentReader;

    @DistributedLock(key = "#commentId") // 대부분은 분산락으로 처리
    @OptimisticLock(retryCount = 3, waitTime = 1000L) // 만약의 경우를 대비해서 낙관적락도 걸기
    @Transactional
    public void saveCommentLike(final Long userId, final Long commentId) {

        Comment comment = commentReader.findByIdWithLock(commentId); // 댓글 조회 (비관적 락)
        commentLikeValidator.validateDuplicatedLike(commentId, userId); // commentLike 테이블 조회 -> 자신이 좋아요를 눌렀는지 기록 필요
        comment.addLike();
        commentLikeWriter.save(userId, comment);
    }

    @DistributedLock(key = "#commentId") // 대부분은 분산락으로 처리
    @OptimisticLock(retryCount = 3, waitTime = 1000L) // 만약의 경우를 대비해서 낙관적락도 걸기
    @Transactional
    public void deleteCommentLike(final Long userId, final Long commentId) {

        Comment comment = commentReader.findByIdWithLock(commentId);
        comment.unLike();
        CommentLike commentLike = commentLikeReader.find(userId, commentId);
        commentLikeWriter.delete(commentLike);
    }

}
