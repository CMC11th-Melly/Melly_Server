package cmc.mellyserver.domain.comment;

import java.util.List;
import java.util.Objects;

import org.springframework.stereotype.Component;

import cmc.mellyserver.dbcore.comment.comment.Comment;
import cmc.mellyserver.dbcore.comment.comment.CommentRepository;
import cmc.mellyserver.dbcore.memory.Memory;
import cmc.mellyserver.dbcore.user.User;
import cmc.mellyserver.domain.comment.dto.request.CommentRequestDto;
import cmc.mellyserver.domain.memory.MemoryReader;
import cmc.mellyserver.domain.user.UserReader;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class CommentWriter {

    private final CommentRepository commentRepository;

    private final UserReader userReader;

    private final MemoryReader memoryReader;

    private final CommentReader commentReader;

    public Comment save(CommentRequestDto commentRequestDto) {

        Comment parentComment = findRoot(commentRequestDto.getRootId());
        return saveComment(parentComment, commentRequestDto);
    }

    public void remove(Long commentId) {
        Comment comment = commentReader.findById(commentId);
        comment.delete();
        List<Comment> children = comment.getChildren();
        children.forEach(Comment::delete);
    }

    public void update(Long commentId, String content) {
        Comment comment = commentReader.findById(commentId);
        comment.update(content);
    }

    private Comment saveComment(Comment rootComment, CommentRequestDto commentRequestDto) {

        User user = userReader.findById(commentRequestDto.getUserId());
        Memory memory = memoryReader.findById(commentRequestDto.getMemoryId());

        if (Objects.isNull(rootComment)) {
            return commentRepository.save(
                Comment.createRoot(commentRequestDto.getContent(), user, memory.getId(), null));
        }

        User mentionUser = userReader.findById(commentRequestDto.getMentionId());

        Comment comment = commentRepository.save(
            Comment.createChild(commentRequestDto.getContent(), user, mentionUser, memory.getId(), rootComment));
        rootComment.getChildren().add(comment);
        return comment;
    }

    private Comment findRoot(Long rootId) {

        if (Objects.isNull(rootId)) {
            return null;
        }

        return commentReader.findById(rootId);
    }
}
