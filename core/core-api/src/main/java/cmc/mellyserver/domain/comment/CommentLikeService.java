package cmc.mellyserver.domain.comment;

import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cmc.mellyserver.dbcore.comment.commenlike.CommentLike;
import cmc.mellyserver.dbcore.comment.comment.Comment;
import cmc.mellyserver.dbcore.user.User;
import cmc.mellyserver.domain.comment.event.CommentLikeEvent;
import cmc.mellyserver.domain.user.UserReader;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CommentLikeService {

    private final CommentLikeReader commentLikeReader;

    private final CommentLikeWriter commentLikeWriter;

    private final CommentLikeValidator commentLikeValidator;

    private final CommentReader commentReader;

    private final UserReader userReader;

    private final ApplicationEventPublisher publisher;

    @Transactional
    public void saveCommentLike(final Long userId, final Long commentId) {

        Comment comment = commentReader.findById(commentId);
        User writer = userReader.findById(userId);
        commentLikeValidator.validateDuplicatedLike(commentId, userId);
        commentLikeWriter.save(writer, comment);
        publisher.publishEvent(new CommentLikeEvent(userId, comment.getMemoryId(), comment.getUser().getNickname()));
    }

    @Transactional
    public void deleteCommentLike(final Long userId, final Long commentId) {

        CommentLike commentLike = commentLikeReader.find(userId, commentId);
        commentLikeWriter.delete(commentLike);
    }

}
