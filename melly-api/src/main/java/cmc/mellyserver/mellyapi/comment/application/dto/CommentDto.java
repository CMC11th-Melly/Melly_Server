package cmc.mellyserver.mellyapi.comment.application.dto;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonFormat;

import cmc.mellyserver.mellycore.comment.domain.Comment;
import cmc.mellyserver.mellycore.user.domain.User;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CommentDto implements Serializable {

	private Long id;

	private String content;

	private boolean loginUserWrite;

	private boolean loginUserLike;

	private int likeCount;

	private String mentionUserName;

	private Long writerId;

	private String nickname;

	private String profileImage;

	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyyMMddHHmm")
	private LocalDateTime createdDate;
	private List<CommentDto> children = new ArrayList<>();

	public CommentDto(Long id, String content, Boolean isLoginUserWrite, Boolean isLoginUserLike, int likeCount,
		String mentionUserName, Long writerId, String nickname, String profileImage, LocalDateTime createdDate) {
		this.id = id;
		this.content = content;
		this.loginUserWrite = isLoginUserWrite;
		this.loginUserLike = isLoginUserLike;
		this.likeCount = likeCount;
		this.createdDate = createdDate;
		this.mentionUserName = mentionUserName;
		this.writerId = writerId;
		this.nickname = nickname;
		this.profileImage = profileImage;
	}

	// TODO : 조치 필요
	public static CommentDto convertCommentToDto(Comment comment, User user, String mentionUserName) {

		//        CommentDto commentDto =  comment.getIsDeleted() == DeleteStatus.Y ?
		//                new CommentDto(comment.getId(), "삭제된 댓글입니다.", false,false,0,null, null,null,null,null) :
		//                new CommentDto(comment.getId(), comment.getContent(),false,false,comment.getCommentLikes().size(),mentionUserName,comment.getWriterId(), comment.getWriter().getNickname(),comment.getWriter().getProfileImage(),comment.getCreatedDate());

		//        if(comment.getWriter().getUserId().equals(user.getUserId()))
		//        {
		//            commentDto.setLoginUserWrite(true);
		//        }
		//        if(comment.getCommentLikes().stream().anyMatch(cl -> user.getCommentLikes().contains(cl)))
		//        {
		//            commentDto.setLoginUserLike(true);
		//        }
		//        return commentDto;
		return null;
	}
}