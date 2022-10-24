package cmc.mellyserver.comment.application.dto;

import cmc.mellyserver.comment.domain.Comment;
import cmc.mellyserver.comment.domain.DeleteStatus;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CommentDto implements Serializable {


    private Long id;
    private String content;
    private Boolean isLoginUser;
    private String nickname;
    private String profileImage;
    @Schema(example = "202210140920")
    @JsonFormat(shape= JsonFormat.Shape.STRING, pattern="yyyyMMddHHmm")
    private LocalDateTime createdDate;
    private List<CommentDto> children = new ArrayList<>();


    public CommentDto(Long id, String content,Boolean isLoginUser, String nickname,String profileImage,LocalDateTime createdDate) {
        this.id = id;
        this.content = content;
        this.isLoginUser = isLoginUser;
        this.createdDate = createdDate;
        this.nickname = nickname;
        this.profileImage = profileImage;
    }


    public static CommentDto convertCommentToDto(Comment comment,String uid) {

        CommentDto commentDto =  comment.getIsDeleted() == DeleteStatus.Y ?
                new CommentDto(comment.getId(), "삭제된 댓글입니다.", false, null,null,null) :
                new CommentDto(comment.getId(), comment.getContent(),false, comment.getWriter().getNickname(),comment.getWriter().getProfileImage(),comment.getCreatedDate());

        if(comment.getWriter().getUserId().equals(uid))
        {
            commentDto.setIsLoginUser(true);
            return commentDto;
        }
        return commentDto;
    }
}