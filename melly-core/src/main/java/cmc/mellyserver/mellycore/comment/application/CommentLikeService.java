package cmc.mellyserver.mellycore.comment.application;

import cmc.mellyserver.mellycore.comment.application.event.CommentLikeEvent;
import cmc.mellyserver.mellycore.comment.domain.Comment;
import cmc.mellyserver.mellycore.comment.domain.CommentLike;
import cmc.mellyserver.mellycore.comment.domain.repository.CommentLikeRepository;
import cmc.mellyserver.mellycore.comment.domain.repository.CommentRepository;
import cmc.mellyserver.mellycore.comment.exception.CommentLikeNotFoundException;
import cmc.mellyserver.mellycore.comment.exception.CommentNotFoundException;
import cmc.mellyserver.mellycore.comment.exception.DuplicatedCommentLikeException;
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

        Comment comment = commentRepository.findById(commentId).orElseThrow(CommentNotFoundException::new);
        commentLikeRepository.findCommentLikeByCommentIdAndUserId(commentId, userId)
                .ifPresent(commentLike -> {
                    throw new DuplicatedCommentLikeException();
                });

        commentLikeRepository.save(CommentLike.createCommentLike(userId, comment));
        publisher.publishEvent(new CommentLikeEvent(userId, comment.getUser().getNickname()));
    }

    @Transactional
    public void deleteCommentLike(Long id, Long commentId) {

        CommentLike commentLike = commentLikeRepository.findCommentLikeByCommentIdAndUserId(commentId, id)
                .orElseThrow(CommentLikeNotFoundException::new);
        commentLikeRepository.delete(commentLike);
    }
}
