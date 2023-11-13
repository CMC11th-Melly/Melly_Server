package cmc.mellyserver.domain.comment.dto.response;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

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

  private String mentionUserNickname;

  private String nickname;

  private String profileImage;

  @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyyMMddHHmm")
  private LocalDateTime createdDate;

  private List<CommentDto> children;

  public static CommentDto of(Comment comment, User user) {
	if (Objects.isNull(comment.getRoot())) {
	  return CommentDto.createRoot(comment, user);
	}
	return CommentDto.createChild(comment, user);
  }

  private static boolean isLoginUser(Comment comment, User user) {
	return comment.getUser().getId().equals(user.getId());
  }

  public static CommentDto createChild(Comment comment, User user) {

	CommentDto commentDto = new CommentDto(comment.getId(), comment.getContent(), false, false,
		comment.getCommentLikes().size(), comment.getMentionUser().getNickname(), comment.getUser().getNickname(),
		comment.getUser().getProfileImage(), comment.getCreatedDate(), new ArrayList<>());

	if (isLoginUser(comment, user)) {
	  commentDto.setCurrentUser(true);
	}

	return commentDto;
  }

  public static CommentDto createRoot(Comment comment, User user) {

	CommentDto commentDto = new CommentDto(comment.getId(), comment.getContent(), false, false,
		comment.getCommentLikes().size(), null, comment.getUser().getNickname(),
		comment.getUser().getProfileImage(), comment.getCreatedDate(), new ArrayList<>());

	if (isLoginUser(comment, user)) {
	  commentDto.setCurrentUser(true);
	}

	return commentDto;
  }

  public void setCurrentUserLike(boolean currentUserLike) {
	isCurrentUserLike = currentUserLike;
  }
}