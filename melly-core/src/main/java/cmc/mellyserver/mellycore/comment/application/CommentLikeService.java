package cmc.mellyserver.mellycore.comment.application;

import cmc.mellyserver.mellycore.comment.domain.Comment;
import cmc.mellyserver.mellycore.comment.domain.CommentLike;
import cmc.mellyserver.mellycore.comment.domain.repository.CommentLikeRepository;
import cmc.mellyserver.mellycore.comment.domain.repository.CommentRepository;
import cmc.mellyserver.mellycore.comment.exception.CommentLikeNotFoundException;
import cmc.mellyserver.mellycore.comment.exception.CommentNotFoundException;
import cmc.mellyserver.mellycore.comment.exception.DuplicatedCommentLikeException;
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

        Comment comment = commentRepository.findById(commentId).orElseThrow(CommentNotFoundException::new);
        commentLikeRepository.findCommentLikeByCommentIdAndUserId(commentId, userSeq)
                .ifPresent(commentLike -> {
                    throw new DuplicatedCommentLikeException();
                });
        return commentLikeRepository.save(CommentLike.createCommentLike(userSeq, comment));
    }

    @Transactional
    public void deleteCommentLike(Long userSeq, Long commentId) {

        CommentLike commentLike = commentLikeRepository.findCommentLikeByCommentIdAndUserId(commentId, userSeq)
                .orElseThrow(CommentLikeNotFoundException::new);
        commentLikeRepository.delete(commentLike);
    }
}
