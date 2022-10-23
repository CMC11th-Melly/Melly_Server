package cmc.mellyserver.comment.application;

import cmc.mellyserver.comment.application.dto.CommentDto;
import cmc.mellyserver.comment.domain.Comment;
import cmc.mellyserver.comment.domain.CommentQueryRepository;
import cmc.mellyserver.comment.domain.CommentRepository;
import cmc.mellyserver.comment.domain.DeleteStatus;
import cmc.mellyserver.comment.presentation.dto.CommentRequest;
import cmc.mellyserver.common.exception.ExceptionCodeAndDetails;
import cmc.mellyserver.common.exception.GlobalBadRequestException;
import cmc.mellyserver.common.util.auth.AuthenticatedUserChecker;
import cmc.mellyserver.memory.domain.Memory;
import cmc.mellyserver.memory.domain.MemoryRepository;
import cmc.mellyserver.user.domain.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static cmc.mellyserver.comment.application.dto.CommentDto.convertCommentToDto;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CommentService {

     private final CommentQueryRepository commentQueryRepository;
     private final CommentRepository commentRepository;
     private final AuthenticatedUserChecker authenticatedUserChecker;
     private final MemoryRepository memoryRepository;


     public List<CommentDto> getComment(String uid,Long memoryId)
     {
         User user = authenticatedUserChecker.checkAuthenticatedUserExist(uid);
         List<Comment> comment = commentQueryRepository.findComment(memoryId);
         return convertNestedStructure(comment,user);
     }


    @Transactional
    public void saveComment(String uid, CommentRequest commentRequest) {

        User user = authenticatedUserChecker.checkAuthenticatedUserExist(uid);
        Memory memory = memoryRepository.findById(commentRequest.getMemoryId()).orElseThrow(() -> {
            throw new GlobalBadRequestException(ExceptionCodeAndDetails.NO_SUCH_MEMORY);
        });
        Comment parent = commentRequest.getParentId() != null ? commentRepository.findById(commentRequest.getParentId()).orElseThrow(() -> {
            throw new GlobalBadRequestException(ExceptionCodeAndDetails.NO_SUCH_COMMENT);
        }) : null;
        commentRepository.save(Comment.createComment(commentRequest.getContent(),user,memory,parent));
    }

    @Transactional
    public void updateComment(Long commentId, String content)
    {
        Comment comment = commentRepository.findById(commentId).orElseThrow(() -> {
            throw new GlobalBadRequestException(ExceptionCodeAndDetails.NO_SUCH_COMMENT);
        });
        comment.updateComment(content);
    }


    @Transactional
    public void deleteComment(Long commentId) {

        Comment comment = commentRepository.findCommentByIdWithParent(commentId).orElseThrow(
                () -> {throw new GlobalBadRequestException(ExceptionCodeAndDetails.NO_SUCH_COMMENT);});

        if(comment.getChildren().size() != 0) { // 자식이 있으면 상태만 변경
            comment.changeDeletedStatus(DeleteStatus.Y);
        } else { // 삭제 가능한 조상 댓글을 구해서 삭제
            commentRepository.delete(getDeletableAncestorComment(comment));
        }
    }


    private Comment getDeletableAncestorComment(Comment comment) { // 삭제 가능한 조상 댓글을 구함
        Comment parent = comment.getParent(); // 현재 댓글의 부모를 구함
        if(parent != null && parent.getChildren().size() == 1 && parent.getIsDeleted() == DeleteStatus.Y)
            // 부모가 있고, 부모의 자식이 1개(지금 삭제하는 댓글)이고, 부모의 삭제 상태가 TRUE인 댓글이라면 재귀
            return getDeletableAncestorComment(parent);
        return comment; // 삭제해야하는 댓글 반환
    }


    private List<CommentDto> convertNestedStructure(List<Comment> comments,User user) {

        List<CommentDto> result = new ArrayList<>();
        Map<Long, CommentDto> map = new HashMap<>();

        comments.stream().forEach(c -> {
            // 댓글 dto 만들고
            CommentDto dto = convertCommentToDto(c,user.getUserId());
            // 그 댓글을 map에 넣고
            map.put(dto.getId(), dto);
            // 만약 부모 댓글이 있다면?
            if(c.getParent() != null)
            {
                // dto로 뿌려줄때 연관관게 맺는건가...? 부모가 누군지만 관리!
                map.get(c.getParent().getId()).getChildren().add(dto);
            }
            else
            {
                result.add(dto);
            }
        });
        return result;
    }


}