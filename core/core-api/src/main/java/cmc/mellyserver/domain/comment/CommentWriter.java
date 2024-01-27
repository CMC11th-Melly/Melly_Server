package cmc.mellyserver.domain.comment;

import java.util.Objects;

import org.springframework.stereotype.Component;

import cmc.mellyserver.dbcore.comment.comment.Comment;
import cmc.mellyserver.dbcore.comment.comment.CommentRepository;
import cmc.mellyserver.dbcore.memory.memory.Memory;
import cmc.mellyserver.dbcore.user.User;
import cmc.mellyserver.domain.comment.dto.request.CommentRequestDto;
import cmc.mellyserver.domain.memory.MemoryReader;
import cmc.mellyserver.domain.user.UserReader;
import cmc.mellyserver.support.exception.BusinessException;
import cmc.mellyserver.support.exception.ErrorCode;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class CommentWriter {

    private final CommentRepository commentRepository;

    private final UserReader userReader;

    private final MemoryReader memoryReader;

    private final CommentReader commentReader;

    public Comment save(CommentRequestDto commentRequestDto) {

        User user = userReader.findById(commentRequestDto.getUserId());
        Memory memory = memoryReader.read(commentRequestDto.getMemoryId());
        Comment root = findRoot(commentRequestDto.getRootId());

        if (Objects.isNull(root)) {
            return commentRepository.save(Comment.createRoot(commentRequestDto.getContent(), user, memory.getId()));
        }

        User mentionUser = userReader.findById(commentRequestDto.getMentionId());
        Comment comment = commentRepository.save(
            Comment.createChild(commentRequestDto.getContent(), user, mentionUser, memory.getId(), root));
        root.getChildren().add(comment);
        return comment;
    }

    public void remove(Long userId, Long commentId) {
        Comment comment = commentReader.findById(commentId);
        checkAuthority(userId, comment);
        commentRepository.delete(comment);
    }

    public void update(Long commentId, String content) {
        Comment comment = commentReader.findById(commentId);
        comment.update(content);
    }

    private Comment findRoot(Long rootId) {

        if (Objects.isNull(rootId)) {
            return null;
        }

        return commentReader.findById(rootId);
    }

    private void checkAuthority(Long userId, Comment comment) {
        if (!comment.getUser().getId().equals(userId)) {
            throw new BusinessException(ErrorCode.NOT_VALID_ERROR);
        }
    }
}
