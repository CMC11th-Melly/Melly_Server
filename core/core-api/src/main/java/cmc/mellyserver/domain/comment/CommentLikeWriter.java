package cmc.mellyserver.domain.comment;

import org.springframework.stereotype.Component;

import cmc.mellyserver.common.aspect.lock.OptimisticLock;
import cmc.mellyserver.dbcore.comment.commenlike.CommentLike;
import cmc.mellyserver.dbcore.comment.commenlike.CommentLikeRepository;
import cmc.mellyserver.dbcore.comment.comment.Comment;
import cmc.mellyserver.dbcore.user.User;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class CommentLikeWriter {

    private final CommentLikeRepository commentLikeRepository;

    @OptimisticLock
    public void save(final User user, final Comment comment) {

        comment.addLike();
        commentLikeRepository.save(CommentLike.createCommentLike(user, comment));
    }

    @OptimisticLock
    public void delete(final CommentLike commentLike, final Comment comment) {

        comment.unLike();
        commentLikeRepository.delete(commentLike);
    }
}
