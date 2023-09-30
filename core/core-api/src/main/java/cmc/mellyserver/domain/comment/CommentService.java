package cmc.mellyserver.domain.comment;


import cmc.mellyserver.dbcore.comment.comment.Comment;
import cmc.mellyserver.dbcore.comment.comment.CommentRepository;
import cmc.mellyserver.dbcore.memory.Memory;
import cmc.mellyserver.dbcore.memory.MemoryRepository;
import cmc.mellyserver.dbcore.user.User;
import cmc.mellyserver.dbcore.user.UserRepository;
import cmc.mellyserver.domain.comment.dto.request.CommentRequestDto;
import cmc.mellyserver.domain.comment.dto.response.CommentDto;
import cmc.mellyserver.domain.comment.dto.response.CommentResponseDto;
import cmc.mellyserver.domain.comment.event.CommentEnrollEvent;
import cmc.mellyserver.domain.comment.query.CommentQueryRepository;
import cmc.mellyserver.support.exception.BusinessException;
import cmc.mellyserver.support.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

import static java.lang.Boolean.FALSE;
import static java.lang.Boolean.TRUE;

@Service
@RequiredArgsConstructor
public class CommentService {

    private final CommentQueryRepository commentQueryRepository;

    private final UserRepository userRepository;

    private final ApplicationEventPublisher publisher;

    private final CommentRepository commentRepository;

    private final MemoryRepository memoryRepository;

    @Transactional(readOnly = true)
    public CommentResponseDto getComments(final Long userId, final Long memoryId) {

        User user = userRepository.getById(userId);
        List<Comment> comment = commentQueryRepository.findComment(user.getId(), memoryId);
        return convertNestedStructure(comment, user);
    }

    @Transactional
    public void saveComment(final CommentRequestDto commentRequestDto) {

        Memory memory = memoryRepository.findById(commentRequestDto.getMemoryId()).orElseThrow(() -> {
            throw new BusinessException(ErrorCode.NO_SUCH_MEMORY);
        });
        Comment parentComment = getParentComment(commentRequestDto);

        Comment comment = Objects.isNull(parentComment) ?
                saveCommentWithoutParent(commentRequestDto, memory) :
                saveCommentWithParent(commentRequestDto, memory, parentComment);

        publisher.publishEvent(new CommentEnrollEvent(memory.getUserId(), comment.getMemoryId(), comment.getUser().getNickname()));
    }

    @Transactional
    public Comment updateComment(final Long commentId, final String content) {

        Comment comment = commentRepository.findById(commentId).orElseThrow(() -> {
            throw new BusinessException(ErrorCode.NO_SUCH_COMMENT);
        });
        comment.updateComment(content);
        return comment;
    }

    @Transactional
    public void deleteComment(Long commentId) {

        Comment comment = commentRepository.findCommentByIdWithParent(commentId)
                .orElseThrow(() -> {
                    throw new BusinessException(ErrorCode.NO_SUCH_COMMENT);
                });
        removeCommentAccordingToChildComment(comment);
    }

    private Comment saveCommentWithoutParent(CommentRequestDto commentRequestDto, Memory memory) {
        User user = userRepository.getById(commentRequestDto.getUserId());
        return commentRepository.save(Comment.createComment(commentRequestDto.getContent(), user, memory.getId(), null));
    }

    private Comment saveCommentWithParent(CommentRequestDto commentRequestDto, Memory memory, Comment parentComment) {
        User user = userRepository.getById(commentRequestDto.getUserId());
        Comment childComment = commentRepository.save(Comment.createComment(commentRequestDto.getContent(), user, memory.getId(), parentComment));
        parentComment.getChildren().add(childComment);
        return childComment;
    }

    private Comment getParentComment(CommentRequestDto commentRequestDto) {
        return Objects.isNull(commentRequestDto.getParentId()) ? null : commentRepository.findById(commentRequestDto.getParentId())
                .orElseThrow(() -> {
                    throw new BusinessException(ErrorCode.NO_SUCH_COMMENT);
                });
    }

    private Comment getDeletableAncestorComment(Comment comment) { // 삭제 가능한 조상 댓글을 구함

        Comment parent = comment.getParent(); // 현재 댓글의 부모를 구함
        if (parent != null && parent.getChildren().size() == 1 && parent.getIsDeleted() == TRUE) {
            return getDeletableAncestorComment(parent);
        }
        // 부모가 있고, 부모의 자식이 1개(지금 삭제하는 댓글)이고, 부모의 삭제 상태가 TRUE인 댓글이라면 재귀
        return comment; // 삭제해야하는 댓글 반환

    }

    private CommentResponseDto convertNestedStructure(List<Comment> comments, User user) {

        // CommentDto를 닮을 리스트
        List<CommentDto> result = new ArrayList<>();
        Map<Long, CommentDto> map = new HashMap<>();

        // 삭제 되지 않은 리스트 개수
        int cnt = (int) comments.stream().filter(c -> c.getIsDeleted().equals(FALSE)).count();

        comments.stream().forEach(comment -> {

            CommentDto dto = CommentDto.convertCommentToDto(comment, user);

            // 그 댓글을 map에 넣고
            map.put(dto.getId(), dto);

            // 만약 부모가 있다면
            if (comment.getParent() != null) {
                // dto로 뿌려줄때 연관관게 맺는건가...? 부모가 누군지만 관리!
                if (map.getOrDefault(comment.getParent().getId(), null) != null) {
                    map.get(comment.getParent().getId()).getChildren().add(dto);
                }

            } else {
                result.add(dto);
            }
        });
        return new CommentResponseDto(cnt, result);
    }

    private void removeCommentAccordingToChildComment(Comment comment) {

        // 만약 자식이 없다면
        if (comment.getChildren().isEmpty()) {

            Comment deletableAncestorComment = getDeletableAncestorComment(comment);
            deletableAncestorComment.delete();
            comment.delete();
        } else {
            comment.removeContent();
        }
    }

}
