package cmc.mellyserver.domain.comment.dto.response;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonFormat;

import cmc.mellyserver.dbcore.comment.comment.Comment;
import cmc.mellyserver.dbcore.user.User;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CommentDto implements Serializable {

	private static final String REMOVE_COMMENT = "삭제된 댓글입니다";
	private Long id;

	private String content;

	private boolean isCurrentUser;

	private boolean isCurrentUserLike;

	private int likeCount;

	private String nickname;

	private String profileImage;

	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyyMMddHHmm")
	private LocalDateTime createdDate;

	private List<CommentDto> children = new ArrayList<>();

	public static CommentDto convertCommentToDto(Comment comment, User user) {

		if (comment.getIsDeleted()) {
			return CommentDto.removedComment(comment);
		}

		return CommentDto.create(comment, user);
	}

	private static boolean isLoginUser(Comment comment, User user) {
		return comment.getUser().getId().equals(user.getId());
	}

	public static CommentDto removedComment(Comment comment) {
		return new CommentDto(comment.getId(), REMOVE_COMMENT, false, false, 0, null, null, null,
			Collections.emptyList());
	}

	public static CommentDto create(Comment comment, User user) {
		CommentDto commentDto = new CommentDto(comment.getId(), comment.getContent(), false, false,
			comment.getCommentLikes().size(), comment.getUser().getNickname(), comment.getUser().getProfileImage(),
			comment.getCreatedDate(), Collections.emptyList());

		if (isLoginUser(comment, user)) {
			commentDto.setCurrentUserLike(true);
		}

		return commentDto;
	}

	private void setCurrentUserLike(boolean currentUserLike) {
		isCurrentUserLike = currentUserLike;
	}
}