package cmc.mellyserver.mellycore.comment.application;


import cmc.mellyserver.mellycommon.codes.ErrorCode;
import cmc.mellyserver.mellycommon.enums.DeleteStatus;
import cmc.mellyserver.mellycore.comment.application.dto.request.CommentRequestDto;
import cmc.mellyserver.mellycore.comment.application.dto.response.CommentDto;
import cmc.mellyserver.mellycore.comment.application.dto.response.CommentResponseDto;
import cmc.mellyserver.mellycore.comment.application.event.CommentEnrollEvent;
import cmc.mellyserver.mellycore.comment.domain.Comment;
import cmc.mellyserver.mellycore.comment.domain.repository.CommentQueryRepository;
import cmc.mellyserver.mellycore.comment.domain.repository.CommentRepository;
import cmc.mellyserver.mellycore.comment.exception.CommentNotFoundException;
import cmc.mellyserver.mellycore.common.AuthenticatedUserChecker;
import cmc.mellyserver.mellycore.common.exception.BusinessException;
import cmc.mellyserver.mellycore.memory.domain.Memory;
import cmc.mellyserver.mellycore.memory.domain.repository.MemoryRepository;
import cmc.mellyserver.mellycore.user.domain.User;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Service
@RequiredArgsConstructor
public class CommentService {

    private final CommentQueryRepository commentQueryRepository;

    private final AuthenticatedUserChecker authenticatedUserChecker;

    private final ApplicationEventPublisher publisher;

    private final CommentRepository commentRepository;

    private final MemoryRepository memoryRepository;

    @Transactional(readOnly = true)
    public CommentResponseDto getComments(Long userId, Long memoryId) {

        User user = authenticatedUserChecker.checkAuthenticatedUserExist(userId);
        List<Comment> comment = commentQueryRepository.findComment(user.getId(), memoryId);
        return convertNestedStructure(comment, user);
    }

    @Transactional
    public void saveComment(CommentRequestDto commentRequestDto) {

        Memory memory = memoryRepository.findById(commentRequestDto.getMemoryId()).orElseThrow(() -> {
            throw new BusinessException(ErrorCode.NO_SUCH_MEMORY);
        });
        Comment parentComment = getParentComment(commentRequestDto);

        Comment comment = Objects.isNull(parentComment) ?
                saveCommentWithoutParent(commentRequestDto, memory) :
                saveCommentWithParent(commentRequestDto, memory, parentComment);

        publisher.publishEvent(new CommentEnrollEvent(memory.getUserId(), comment.getUser().getNickname()));
    }

    @Transactional
    public Comment updateComment(Long commentId, String content) {

        Comment comment = commentRepository.findById(commentId).orElseThrow(CommentNotFoundException::new);
        comment.updateComment(content);
        return comment;
    }

    @Transactional
    public void deleteComment(Long commentId) {

        Comment comment = commentRepository.findCommentByIdWithParent(commentId).orElseThrow(CommentNotFoundException::new);
        removeCommentAccordingToChildComment(comment);
    }

    private Comment saveCommentWithoutParent(CommentRequestDto commentRequestDto, Memory memory) {
        User user = authenticatedUserChecker.checkAuthenticatedUserExist(commentRequestDto.getUserId());
        return commentRepository.save(Comment.createComment(commentRequestDto.getContent(), user, memory.getId(), null));
    }

    private Comment saveCommentWithParent(CommentRequestDto commentRequestDto, Memory memory, Comment parentComment) {
        User user = authenticatedUserChecker.checkAuthenticatedUserExist(commentRequestDto.getUserId());
        Comment childComment = commentRepository.save(Comment.createComment(commentRequestDto.getContent(), user, memory.getId(), parentComment));
        parentComment.getChildren().add(childComment);
        return childComment;
    }

    private Comment getParentComment(CommentRequestDto commentRequestDto) {
        return Objects.isNull(commentRequestDto.getParentId()) ? null : commentRepository.findById(commentRequestDto.getParentId()).orElseThrow(CommentNotFoundException::new);
    }

    private Comment getDeletableAncestorComment(Comment comment) { // 삭제 가능한 조상 댓글을 구함

        Comment parent = comment.getParent(); // 현재 댓글의 부모를 구함
        if (parent != null && parent.getChildren().size() == 1 && parent.getIsDeleted() == DeleteStatus.Y) {
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
        int cnt = (int) comments.stream().filter(c -> c.getIsDeleted().equals(DeleteStatus.N)).count();

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
