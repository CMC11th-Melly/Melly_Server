package cmc.mellyserver.comment.application;

import cmc.mellyserver.comment.application.dto.CommentResponseDto;
import cmc.mellyserver.comment.domain.*;
import cmc.mellyserver.comment.domain.repository.CommentLikeRepository;
import cmc.mellyserver.comment.domain.repository.CommentQueryRepository;
import cmc.mellyserver.comment.domain.repository.CommentRepository;
import cmc.mellyserver.comment.presentation.dto.CommentRequest;
import cmc.mellyserver.common.exception.ExceptionCodeAndDetails;
import cmc.mellyserver.common.exception.GlobalBadRequestException;
import cmc.mellyserver.common.util.auth.AuthenticatedUserChecker;
import cmc.mellyserver.memory.domain.Memory;
import cmc.mellyserver.memory.infrastructure.data.MemoryRepository;
import cmc.mellyserver.user.domain.User;
import cmc.mellyserver.user.domain.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CommentService {

     private final CommentQueryRepository commentQueryRepository;
     private final CommentRepository commentRepository;
     private final UserRepository userRepository;
     private final AuthenticatedUserChecker authenticatedUserChecker;
     private final CommentLikeRepository commentLikeRepository;
    private final MemoryRepository memoryRepository;


    public CommentResponseDto getComment(Long userSeq,Long memoryId)
     {
//         User user = authenticatedUserChecker.checkAuthenticatedUserExist(userSeq);
//         List<Comment> comment = commentQueryRepository.findComment(memoryId,userSeq);
//         return convertNestedStructure(comment,user);
         return null;
     }


    @Transactional
    public void saveComment(Long userSeq, CommentRequest commentRequest) {

        Memory memory = memoryRepository.findById(commentRequest.getMemoryId()).orElseThrow(() -> {
            throw new GlobalBadRequestException(ExceptionCodeAndDetails.NO_SUCH_MEMORY);
        });


//        Comment parent = commentRequest.getParentId() != null ? commentRepository.findById(commentRequest.getParentId()).orElseThrow(() -> {
//            throw new GlobalBadRequestException(ExceptionCodeAndDetails.NO_SUCH_COMMENT);
//        }) : null;
//
//        commentRepository.save(Comment.createComment(commentRequest.getContent(),userSeq,commentRequest.getMemoryId(),parent,commentRequest.getMentionUserId()));


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

//        Comment comment = commentRepository.findCommentByIdWithParent(commentId).orElseThrow(
//                () -> {throw new GlobalBadRequestException(ExceptionCodeAndDetails.NO_SUCH_COMMENT);});
//
//        if(comment.getChildren().size() != 0) { // 자식이 있으면 상태만 변경
//            comment.changeDeletedStatus(DeleteStatus.Y);
//        } else { // 삭제 가능한 조상 댓글을 구해서 삭제
//            commentRepository.delete(getDeletableAncestorComment(comment));
//        }
    }



    @Transactional
    public void saveCommentLike(Long userSeq, Long commentId) {

        Comment comment = commentRepository.findById(commentId).orElseThrow(() -> {
            throw new GlobalBadRequestException(ExceptionCodeAndDetails.NO_SUCH_COMMENT);
        });
        CommentLike commentLike = CommentLike.createCommentLike(userSeq, comment);
        commentLikeRepository.save(commentLike);
    }



    @Transactional
    public void deleteCommentLike(Long userSeq, Long commentId) {

        CommentLike commentLike = commentLikeRepository.findCommentLikeByCommentIdAndUserId(commentId,userSeq).orElseThrow(() -> {
            throw new GlobalBadRequestException(ExceptionCodeAndDetails.NO_SUCH_COMMENT_LIKE);
        });
        commentLikeRepository.delete(commentLike);
    }



    private Comment getDeletableAncestorComment(Comment comment) { // 삭제 가능한 조상 댓글을 구함
//        Comment parent = comment.getParent(); // 현재 댓글의 부모를 구함
//        if(parent != null && parent.getChildren().size() == 1 && parent.getIsDeleted() == DeleteStatus.Y)
//            // 부모가 있고, 부모의 자식이 1개(지금 삭제하는 댓글)이고, 부모의 삭제 상태가 TRUE인 댓글이라면 재귀
//            return getDeletableAncestorComment(parent);
//        return comment; // 삭제해야하는 댓글 반환
    return null;
    }

    private CommentResponseDto convertNestedStructure(List<Comment> comments,User user) {
//
//        List<CommentDto> result = new ArrayList<>();
//        Map<Long, CommentDto> map = new HashMap<>();
//
//        int cnt = (int) comments.stream().filter(c -> c.getIsDeleted().equals(DeleteStatus.N)).count();
//
//        comments.stream().forEach(c -> {
//
//            CommentDto dto;
//
//            if(c.getMetionUser() != null)
//            {
//                User mentionUser = userRepository.findById(c.getMetionUser()).orElseThrow(() -> {
//                    throw new GlobalBadRequestException(ExceptionCodeAndDetails.NO_SUCH_USER);
//                });
//
//                dto = convertCommentToDto(c,user,mentionUser.getNickname());
//            }
//            else
//            {
//                dto = convertCommentToDto(c,user,null);
//            }
//
//
//            // 그 댓글을 map에 넣고
//            map.put(dto.getId(), dto);
//            // 만약 부모 댓글이 있다면?
//            if(c.getParent() != null)
//            {
//                // dto로 뿌려줄때 연관관게 맺는건가...? 부모가 누군지만 관리!
//                if(map.getOrDefault(c.getParent().getId(),null) != null)
//                {
//                    map.get(c.getParent().getId()).getChildren().add(dto);
//                }
//
//            }
//            else
//            {
//                result.add(dto);
//            }
//        });
//
//        return new CommentResponseDto(cnt,result);
        return null;
    }

}
