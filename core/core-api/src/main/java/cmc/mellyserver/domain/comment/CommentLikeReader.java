package cmc.mellyserver.domain.comment;

import org.springframework.stereotype.Component;

import cmc.mellyserver.dbcore.comment.commenlike.CommentLike;
import cmc.mellyserver.dbcore.comment.commenlike.CommentLikeRepository;
import cmc.mellyserver.support.exception.CommonException;
import cmc.mellyserver.support.exception.ErrorCode;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class CommentLikeReader {

    private final CommentLikeRepository commentLikeRepository;

    public CommentLike find(final Long userId, final Long commentId) {
        return commentLikeRepository.findByUserIdAndCommentId(userId, commentId)
            .orElseThrow(() -> new CommonException(
                ErrorCode.NOT_EXIST_SCRAP));
    }
}
