package cmc.mellyserver.domain.comment;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

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

    public Comment findByIdWithLock(final Long commentId) {
        return commentRepository.findByOptimisticLock(commentId);
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
        Map<Long, CommentDto> totalComments = new ConcurrentHashMap<>();
        Set<Long> commentIds = extractCommentIdsUserLike(user);

        comments.forEach(comment -> {
                CommentDto commentDto = CommentDto.of(comment, user);
                isUserLike(commentIds, commentDto);
                totalComments.put(commentDto.getId(), commentDto);

                if (isRoot(comment)) {
                    rootComments.add(commentDto);
                } else {
                    totalComments.get(comment.getRoot().getId()).getChildren().add(commentDto);
                }
            }
        );
        return rootComments;
    }

    private void isUserLike(Set<Long> commentIds, CommentDto commentDto) {
        if (commentIds.contains(commentDto.getId())) {
            commentDto.setCurrentUserLike(true);
        }
    }

    private Set<Long> extractCommentIdsUserLike(User user) {
        List<CommentLike> currentUserCommentLike = commentLikeRepository.findByUserId(user.getId());
        return currentUserCommentLike.stream()
            .map(commentLike -> commentLike.getComment().getId())
            .collect(Collectors.toSet());
    }

    private int calculateTotalCommentCount(List<Comment> comments) {
        return (int)comments.stream().filter(c -> Objects.isNull(c.isDeleted())).count();
    }

    private boolean isRoot(Comment comment) {
        return Objects.isNull(comment.getRoot());
    }
}
