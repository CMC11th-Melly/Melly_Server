package cmc.mellyserver.domain.comment;

import org.springframework.stereotype.Component;

import cmc.mellyserver.dbcore.comment.commenlike.CommentLikeRepository;
import cmc.mellyserver.support.exception.BusinessException;
import cmc.mellyserver.support.exception.ErrorCode;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class CommentLikeValidator {

    private final CommentLikeRepository commentLikeRepository;

    public void validateDuplicatedLike(final Long commentId, final Long userId) {
        commentLikeRepository.findByUserIdAndCommentId(commentId, userId).ifPresent(commentLike -> {
            throw new BusinessException(ErrorCode.DUPLICATED_COMMENT_LIKE);
        });
    }

}

