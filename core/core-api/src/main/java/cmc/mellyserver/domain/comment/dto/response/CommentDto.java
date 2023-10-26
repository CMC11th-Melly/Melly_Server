package cmc.mellyserver.domain.comment.dto.response;

import cmc.mellyserver.dbcore.comment.comment.Comment;
import cmc.mellyserver.dbcore.user.User;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static java.lang.Boolean.TRUE;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CommentDto implements Serializable {

	private Long id;

	private String content;

	private boolean loginUserWrite;

	private boolean loginUserLike;

	private int likeCount;

	private String nickname;

	private String profileImage;

	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyyMMddHHmm")
	private LocalDateTime createdDate;

	private List<CommentDto> children = new ArrayList<>();

	// public CommentDto(Long id, String content, Boolean isLoginUserWrite, Boolean
	// isLoginUserLike, int likeCount, Long writerId, String nickname, String
	// profileImage, LocalDateTime createdDate) {
	// this.id = id;
	// this.content = content;
	// this.loginUserWrite = isLoginUserWrite;
	// this.loginUserLike = isLoginUserLike;
	// this.likeCount = likeCount;
	// this.createdDate = createdDate;
	// this.writerId = writerId;
	// this.nickname = nickname;
	// this.profileImage = profileImage;
	// }

	public static CommentDto convertCommentToDto(Comment comment, User user) {
		//
		CommentDto commentDto = comment.getIsDeleted() == TRUE
				? new CommentDto(comment.getId(), "삭제된 댓글입니다.", false, false, 0, null, null, null, new ArrayList<>())
				: new CommentDto(comment.getId(), comment.getContent(), false, false, comment.getCommentLikes().size(),
						comment.getUser().getNickname(), comment.getUser().getProfileImage(), comment.getCreatedDate(),
						new ArrayList<>());

		if (isLoginUser(comment, user)) {
			commentDto.setLoginUserWrite(true);
		}

		return commentDto;
	}

	private static boolean isLoginUser(Comment comment, User user) {
		return comment.getUser().getId().equals(user.getId());
	}

}