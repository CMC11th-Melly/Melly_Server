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

    @OptimisticLock
    @DistributedLock(key = "#commentId")
    @Transactional
    public void saveCommentLike(final Long userId, final Long commentId) {

        Comment comment = commentReader.findByIdWithLock(commentId);
        commentLikeValidator.validateDuplicatedLike(commentId, userId);
        comment.addLike();
        commentLikeWriter.save(userId, comment);
    }

    @OptimisticLock
    @DistributedLock(key = "#commentId")
    @Transactional
    public void deleteCommentLike(final Long userId, final Long commentId) {

        Comment comment = commentReader.findByIdWithLock(commentId);
        comment.unLike();
        CommentLike commentLike = commentLikeReader.find(userId, commentId);
        commentLikeWriter.delete(commentLike);
    }

}
