package cmc.mellyserver.mellyapi.domain.comment;


import cmc.mellyserver.dbcore.comment.commenlike.CommentLike;
import cmc.mellyserver.dbcore.comment.commenlike.CommentLikeRepository;
import cmc.mellyserver.dbcore.comment.comment.Comment;
import cmc.mellyserver.dbcore.comment.comment.CommentRepository;
import cmc.mellyserver.mellyapi.domain.comment.event.CommentLikeEvent;
import cmc.mellyserver.mellyapi.support.exception.BusinessException;
import cmc.mellyserver.mellyapi.support.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class CommentLikeService {

    private final CommentRepository commentRepository;

    private final CommentLikeRepository commentLikeRepository;

    private final ApplicationEventPublisher publisher;

    @Transactional
    public void saveCommentLike(Long userId, Long commentId) {

        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> {
                    throw new BusinessException(ErrorCode.NO_SUCH_COMMENT);
                });

        commentLikeRepository.findCommentLikeByCommentIdAndUserId(commentId, userId)
                .ifPresent(commentLike -> {
                    throw new BusinessException(ErrorCode.DUPLICATED_COMMENT_LIKE);
                });

        commentLikeRepository.save(CommentLike.createCommentLike(userId, comment));
        publisher.publishEvent(new CommentLikeEvent(userId, comment.getMemoryId(), comment.getUser().getNickname()));
    }

    @Transactional
    public void deleteCommentLike(Long id, Long commentId) {

        CommentLike commentLike = commentLikeRepository.findCommentLikeByCommentIdAndUserId(commentId, id)
                .orElseThrow(() -> {
                    throw new BusinessException(ErrorCode.NO_SUCH_COMMENT_LIKE);
                });
        commentLikeRepository.delete(commentLike);
    }
}
