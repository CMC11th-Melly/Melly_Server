package cmc.mellyserver.domain.comment;

import org.springframework.stereotype.Component;

import cmc.mellyserver.dbcore.comment.commenlike.CommentLike;
import cmc.mellyserver.dbcore.comment.commenlike.CommentLikeRepository;
import cmc.mellyserver.dbcore.comment.comment.Comment;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class CommentLikeWriter {

    private final CommentLikeRepository commentLikeRepository;

    public void save(final Long userId, final Comment comment) {
        commentLikeRepository.save(CommentLike.create(userId, comment));
    }

    public void delete(final CommentLike commentLike) {
        commentLikeRepository.delete(commentLike);
    }
}
