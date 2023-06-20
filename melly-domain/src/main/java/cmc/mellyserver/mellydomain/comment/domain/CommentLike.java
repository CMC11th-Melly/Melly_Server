package cmc.mellyserver.mellydomain.comment.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@Table(name = "tb_comment_like")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class CommentLike {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "comment_like_id")
	private Long id;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "comment_id")
	private Comment comment;

	private Long userId; // CommentLike에서 user쪽으로 참조할 일은 없다.

	public static CommentLike createCommentLike(Long userSeq, Comment comment) {
		CommentLike commentLike = new CommentLike();
		commentLike.setComment(comment);
		commentLike.userId = userSeq;
		return commentLike;
	}

	public void setComment(Comment comment) {
		this.comment = comment;
	}

}
