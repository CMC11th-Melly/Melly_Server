package cmc.mellyserver.mellycore.comment.application;


import cmc.mellyserver.mellycommon.enums.DeleteStatus;
import cmc.mellyserver.mellycore.comment.application.dto.request.CommentRequestDto;
import cmc.mellyserver.mellycore.comment.application.dto.response.CommentDto;
import cmc.mellyserver.mellycore.comment.application.dto.response.CommentResponseDto;
import cmc.mellyserver.mellycore.comment.domain.Comment;
import cmc.mellyserver.mellycore.comment.domain.repository.CommentQueryRepository;
import cmc.mellyserver.mellycore.comment.domain.repository.CommentRepository;
import cmc.mellyserver.mellycore.comment.exception.CommentNotFoundException;
import cmc.mellyserver.mellycore.common.AuthenticatedUserChecker;
import cmc.mellyserver.mellycore.memory.domain.Memory;
import cmc.mellyserver.mellycore.memory.domain.repository.MemoryRepository;
import cmc.mellyserver.mellycore.notification.exception.MemoryNotFoundException;
import cmc.mellyserver.mellycore.user.domain.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Service
@RequiredArgsConstructor
public class CommentService {

    private final CommentQueryRepository commentQueryRepository;

    private final AuthenticatedUserChecker authenticatedUserChecker;

    private final CommentRepository commentRepository;

    private final MemoryRepository memoryRepository;

    @Transactional(readOnly = true)
    public CommentResponseDto getComments(Long userSeq, Long memoryId) {

        User user = authenticatedUserChecker.checkAuthenticatedUserExist(userSeq);
        List<Comment> comment = commentQueryRepository.findComment(userSeq, memoryId);
        return convertNestedStructure(comment, user);
    }

    @Transactional
    public Comment saveComment(CommentRequestDto commentRequestDto) {

        Memory memory = memoryRepository.findById(commentRequestDto.getMemoryId()).orElseThrow(MemoryNotFoundException::new);
        Comment parentComment = getParentComment(commentRequestDto);
        return Objects.isNull(parentComment) ? saveCommentWithoutParent(commentRequestDto, memory) : saveCommentWithParent(commentRequestDto, memory, parentComment);
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
        return commentRepository.save(Comment.createComment(commentRequestDto.getContent(), commentRequestDto.getUserId(), memory.getId(), null));
    }

    private Comment saveCommentWithParent(CommentRequestDto commentRequestDto, Memory memory, Comment parentComment) {
        Comment childComment = commentRepository.save(Comment.createComment(commentRequestDto.getContent(), commentRequestDto.getUserId(), memory.getId(), parentComment));
        parentComment.getChildren().add(childComment);
        return childComment;
    }

    private Comment getParentComment(CommentRequestDto commentRequestDto) {
        return Objects.isNull(commentRequestDto.getParentId()) ? null : commentRepository.findById(commentRequestDto.getParentId()).orElseThrow(CommentNotFoundException::new);
    }

    private Comment getDeletableAncestorComment(Comment comment) { // 삭제 가능한 조상 댓글을 구함

        Comment parent = comment.getParent(); // 현재 댓글의 부모를 구함
        if (parent != null && parent.getChildren().size() == 1 && parent.getIsDeleted() == DeleteStatus.Y)
            // 부모가 있고, 부모의 자식이 1개(지금 삭제하는 댓글)이고, 부모의 삭제 상태가 TRUE인 댓글이라면 재귀
            return getDeletableAncestorComment(parent);
        return comment; // 삭제해야하는 댓글 반환

    }

    private CommentResponseDto convertNestedStructure(List<Comment> comments, User user) {

        List<CommentDto> result = new ArrayList<>();
        Map<Long, CommentDto> map = new HashMap<>();

        int cnt = (int) comments.stream().filter(c -> c.getIsDeleted().equals(DeleteStatus.N)).count();

        comments.stream().forEach(c -> {

            CommentDto dto = CommentDto.convertCommentToDto(c, user);

            // 그 댓글을 map에 넣고
            map.put(dto.getId(), dto);

            // 만약 부모 댓글이 있다면?
            if (c.getParent() != null) {
                // dto로 뿌려줄때 연관관게 맺는건가...? 부모가 누군지만 관리!
                if (map.getOrDefault(c.getParent().getId(), null) != null) {
                    map.get(c.getParent().getId()).getChildren().add(dto);
                }

            } else {
                result.add(dto);
            }
        });
        return new CommentResponseDto(cnt, result);
    }

    private void removeCommentAccordingToChildComment(Comment comment) {

        if (comment.getChildren().isEmpty()) {

            Comment deletableAncestorComment = getDeletableAncestorComment(comment);
            deletableAncestorComment.delete();
        } else {
            comment.delete();
        }
    }

}
