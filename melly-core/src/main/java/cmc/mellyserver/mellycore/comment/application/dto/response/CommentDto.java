package cmc.mellyserver.mellycore.comment.application.dto.response;

import cmc.mellyserver.mellycommon.enums.DeleteStatus;
import cmc.mellyserver.mellycore.comment.domain.Comment;
import cmc.mellyserver.mellycore.user.domain.User;
import com.fasterxml.jackson.annotation.JsonFormat;
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

    private boolean loginUserWrite;

    private boolean loginUserLike;

    private int likeCount;

    private Long writerId;

    private String nickname;

    private String profileImage;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyyMMddHHmm")
    private LocalDateTime createdDate;
    private List<CommentDto> children = new ArrayList<>();

    public CommentDto(Long id, String content, Boolean isLoginUserWrite, Boolean isLoginUserLike, int likeCount, Long writerId, String nickname, String profileImage, LocalDateTime createdDate) {
        this.id = id;
        this.content = content;
        this.loginUserWrite = isLoginUserWrite;
        this.loginUserLike = isLoginUserLike;
        this.likeCount = likeCount;
        this.createdDate = createdDate;
        this.writerId = writerId;
        this.nickname = nickname;
        this.profileImage = profileImage;
    }


    public static CommentDto convertCommentToDto(Comment comment, User user) {

        CommentDto commentDto = comment.getIsDeleted() == DeleteStatus.Y ?
                new CommentDto(comment.getId(), "삭제된 댓글입니다.", false, false, 0, null, null, null, null, null) :
                new CommentDto(comment.getId(), comment.getContent(), false, false, comment.getCommentLikes().size(), comment.getWriterId(), comment.getContent(), comment.getContent(), comment.getCreatedDate());

        if (isLoginUser(comment, user)) {
            commentDto.setLoginUserWrite(true);
        }

        return commentDto;

    }

    private static boolean isLoginUser(Comment comment, User user) {
        return comment.getWriterId().equals(user.getUserId());
    }
}