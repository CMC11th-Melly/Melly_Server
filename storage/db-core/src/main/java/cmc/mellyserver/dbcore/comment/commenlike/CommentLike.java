package cmc.mellyserver.dbcore.comment.commenlike;

import cmc.mellyserver.dbcore.comment.comment.Comment;
import cmc.mellyserver.dbcore.user.User;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Table(name = "tb_comment_like")
public class CommentLike {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "comment_like_id")
  private Long id;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "comment_id")
  private Comment comment;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "user_id")
  private User user;

  public static CommentLike createCommentLike(User user, Comment comment) {
	CommentLike commentLike = new CommentLike();
	commentLike.setComment(comment);
	commentLike.setUser(user);
	return commentLike;
  }

  public void setComment(Comment comment) {
	this.comment = comment;
  }

  public void setUser(User user) {
	this.user = user;
  }

}
