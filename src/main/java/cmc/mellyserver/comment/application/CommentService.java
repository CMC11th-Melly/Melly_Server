package cmc.mellyserver.comment.application;

import cmc.mellyserver.comment.application.dto.CommentDto;
import cmc.mellyserver.comment.application.dto.CommentResponseDto;
import cmc.mellyserver.comment.domain.*;
import cmc.mellyserver.comment.presentation.dto.CommentRequest;
import cmc.mellyserver.common.exception.ExceptionCodeAndDetails;
import cmc.mellyserver.common.exception.GlobalBadRequestException;
import cmc.mellyserver.common.util.auth.AuthenticatedUserChecker;
import cmc.mellyserver.memory.domain.Memory;
import cmc.mellyserver.memory.domain.MemoryRepository;
import cmc.mellyserver.user.domain.User;
import cmc.mellyserver.user.domain.UserRepository;
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
     private final UserRepository userRepository;
     private final AuthenticatedUserChecker authenticatedUserChecker;
     private final MemoryRepository memoryRepository;
     private final CommentLikeRepository commentLikeRepository;



     public CommentResponseDto getComment(String uid,Long memoryId)
     {
         User user = authenticatedUserChecker.checkAuthenticatedUserExist(uid);
         List<Comment> comment = commentQueryRepository.findComment(memoryId,user);
         return convertNestedStructure(comment,user);
     }



    @Transactional
    public void saveComment(String uid, CommentRequest commentRequest) {

        // 현재 로그인 사용자
        User user = authenticatedUserChecker.checkAuthenticatedUserExist(uid);
        // 내가 댓글을 남기려는 사람

        Memory memory = memoryRepository.findById(commentRequest.getMemoryId()).orElseThrow(() -> {
            throw new GlobalBadRequestException(ExceptionCodeAndDetails.NO_SUCH_MEMORY);
        });
        Comment parent = commentRequest.getParentId() != null ? commentRepository.findById(commentRequest.getParentId()).orElseThrow(() -> {
            throw new GlobalBadRequestException(ExceptionCodeAndDetails.NO_SUCH_COMMENT);
        }) : null;

        commentRepository.save(Comment.createComment(commentRequest.getContent(),user,memory,parent,commentRequest.getMentionUserId()));
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



    @Transactional
    public void saveCommentLike(String uid, Long commentId) {

        User user = authenticatedUserChecker.checkAuthenticatedUserExist(uid);
        Comment comment = commentRepository.findById(commentId).orElseThrow(() -> {
            throw new GlobalBadRequestException(ExceptionCodeAndDetails.NO_SUCH_COMMENT);
        });
        CommentLike commentLike = CommentLike.createCommentLike(user, comment);
        commentLikeRepository.save(commentLike);
    }



    @Transactional
    public void deleteCommentLike(Long commentId,String uid) {
        User user = authenticatedUserChecker.checkAuthenticatedUserExist(uid);
        CommentLike commentLike = commentLikeRepository.findCommentLikeByCommentIdAndUserUserSeq(commentId,user.getUserSeq()).orElseThrow(() -> {
            throw new GlobalBadRequestException(ExceptionCodeAndDetails.NO_SUCH_COMMENT_LIKE);
        });
        commentLikeRepository.delete(commentLike);
    }



    private Comment getDeletableAncestorComment(Comment comment) { // 삭제 가능한 조상 댓글을 구함
        Comment parent = comment.getParent(); // 현재 댓글의 부모를 구함
        if(parent != null && parent.getChildren().size() == 1 && parent.getIsDeleted() == DeleteStatus.Y)
            // 부모가 있고, 부모의 자식이 1개(지금 삭제하는 댓글)이고, 부모의 삭제 상태가 TRUE인 댓글이라면 재귀
            return getDeletableAncestorComment(parent);
        return comment; // 삭제해야하는 댓글 반환
    }



    private CommentResponseDto convertNestedStructure(List<Comment> comments,User user) {

        List<CommentDto> result = new ArrayList<>();
        Map<Long, CommentDto> map = new HashMap<>();

        int cnt = (int) comments.stream().filter(c -> c.getIsDeleted().equals(DeleteStatus.N)).count();

        comments.stream().forEach(c -> {

            CommentDto dto;

            if(c.getMetionUser() != null)
            {
                User mentionUser = userRepository.findById(c.getMetionUser()).orElseThrow(() -> {
                    throw new GlobalBadRequestException(ExceptionCodeAndDetails.NO_SUCH_USER);
                });

                dto = convertCommentToDto(c,user,mentionUser.getNickname());
            }
            else
            {
                dto = convertCommentToDto(c,user,null);
            }


            // 그 댓글을 map에 넣고
            map.put(dto.getId(), dto);
            // 만약 부모 댓글이 있다면?
            if(c.getParent() != null)
            {
                // dto로 뿌려줄때 연관관게 맺는건가...? 부모가 누군지만 관리!
                if(map.getOrDefault(c.getParent().getId(),null) != null)
                {
                    map.get(c.getParent().getId()).getChildren().add(dto);
                }

            }
            else
            {
                result.add(dto);
            }
        });

        return new CommentResponseDto(cnt,result);
    }

}
