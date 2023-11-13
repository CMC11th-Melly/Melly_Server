package cmc.mellyserver.domain.comment;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import org.springframework.stereotype.Component;

import cmc.mellyserver.dbcore.comment.commenlike.CommentLike;
import cmc.mellyserver.dbcore.comment.commenlike.CommentLikeRepository;
import cmc.mellyserver.dbcore.comment.comment.Comment;
import cmc.mellyserver.dbcore.comment.comment.CommentRepository;
import cmc.mellyserver.dbcore.user.User;
import cmc.mellyserver.domain.comment.dto.response.CommentDto;
import cmc.mellyserver.domain.comment.dto.response.CommentResponseDto;
import cmc.mellyserver.domain.comment.query.CommentQueryRepository;
import cmc.mellyserver.domain.user.UserReader;
import cmc.mellyserver.support.exception.BusinessException;
import cmc.mellyserver.support.exception.ErrorCode;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class CommentReader {

    private final CommentRepository commentRepository;

    private final UserReader userReader;

    private final CommentQueryRepository commentQueryRepository;

    private final CommentLikeRepository commentLikeRepository;

    public Comment findById(final Long commentId) {
        return commentRepository.findById(commentId).orElseThrow(() -> {
            throw new BusinessException(ErrorCode.NO_SUCH_COMMENT);
        });
    }

    public CommentResponseDto findByMemoryId(final Long userId, final Long memoryId) {
        User user = userReader.findById(userId);
        List<Comment> comments = commentQueryRepository.getComments(memoryId);
        int count = calculateTotalCommentCount(comments);
        List<CommentDto> commentDtos = createNestedStructure(comments, user);
        return new CommentResponseDto(count, commentDtos);
    }

    private List<CommentDto> createNestedStructure(List<Comment> comments, User user) {

        List<CommentDto> rootComments = new ArrayList<>();
        Map<Long, CommentDto> totalCommentMap = new HashMap<>();

        List<Long> commentIds = extractCurrentUserLikeComment(user);

        comments.forEach(comment -> {
                CommentDto commentDto = CommentDto.of(comment, user);
                checkCurrentUserLiked(commentIds, commentDto);
                totalCommentMap.put(commentDto.getId(), commentDto);

                if (isChildComment(comment)) {
                    totalCommentMap.get(comment.getRoot().getId()).getChildren().add(commentDto);
                } else {
                    rootComments.add(commentDto);
                }
            }
        );

        return rootComments;
    }

    private void checkCurrentUserLiked(List<Long> commentIds, CommentDto commentDto) {
        if (commentIds.contains(commentDto.getId())) {
            commentDto.setCurrentUserLike(true);
        }
    }

    private List<Long> extractCurrentUserLikeComment(User user) {
        List<CommentLike> currentUserCommentLike = commentLikeRepository.findByUserId(user.getId());
        return currentUserCommentLike.stream()
            .map(commentLike -> commentLike.getComment().getId())
            .toList();
    }

    private int calculateTotalCommentCount(List<Comment> comments) {
        return (int)comments.stream().filter(c -> Objects.isNull(c.getDeletedAt())).count();
    }

    private boolean isChildComment(Comment comment) {
        return Objects.nonNull(comment.getRoot());
    }
}
