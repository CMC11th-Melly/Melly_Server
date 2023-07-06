package cmc.mellyserver.mellycore.comment.application;

import cmc.mellyserver.mellycommon.codes.ErrorCode;
import cmc.mellyserver.mellycore.comment.domain.Comment;
import cmc.mellyserver.mellycore.comment.domain.CommentLike;
import cmc.mellyserver.mellycore.comment.domain.repository.CommentLikeRepository;
import cmc.mellyserver.mellycore.comment.domain.repository.CommentRepository;
import cmc.mellyserver.mellycore.common.exception.GlobalBadRequestException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class CommentLikeService {

    private final CommentRepository commentRepository;

    private final CommentLikeRepository commentLikeRepository;

    @Transactional
    public CommentLike saveCommentLike(Long userSeq, Long commentId) {

        Comment comment = commentRepository.findById(commentId).orElseThrow(() -> {
            throw new GlobalBadRequestException(ErrorCode.NO_SUCH_COMMENT);
        });

        commentLikeRepository.findCommentLikeByCommentIdAndUserId(commentId, userSeq)
                .ifPresent((commentLike -> {
                    throw new GlobalBadRequestException(ErrorCode.DUPLICATED_COMMENT_LIKE);
                }));

        CommentLike commentLike = CommentLike.createCommentLike(userSeq, comment);
        return commentLikeRepository.save(commentLike);
    }

    @Transactional
    public void deleteCommentLike(Long userSeq, Long commentId) {

        CommentLike commentLike = commentLikeRepository.findCommentLikeByCommentIdAndUserId(commentId, userSeq)
                .orElseThrow(() -> {
                    throw new GlobalBadRequestException(ErrorCode.NO_SUCH_COMMENT_LIKE);
                });
        commentLikeRepository.delete(commentLike);
    }
}
