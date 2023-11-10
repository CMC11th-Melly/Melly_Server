package cmc.mellyserver.dbcore.comment.comment;

import java.util.ArrayList;
import java.util.List;

import cmc.mellyserver.dbcore.comment.commenlike.CommentLike;
import cmc.mellyserver.dbcore.config.jpa.JpaBaseEntity;
import cmc.mellyserver.dbcore.user.User;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.Lob;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "tb_comment")
public class Comment extends JpaBaseEntity {

	private static final String REMOVE_COMMENT = "삭제된 댓글입니다.";

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "comment_id")
	private Long id;

	@Column(name = "content", nullable = false)
	@Lob
	private String content;

	@ManyToOne
	@JoinColumn(name = "user_id")
	private User user;

	@Column(name = "memory_id")
	private Long memoryId;

	@OneToMany(mappedBy = "comment", fetch = FetchType.LAZY, cascade = CascadeType.REMOVE, orphanRemoval = true)
	private List<CommentLike> commentLikes = new ArrayList<>();

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "parent_id")
	private Comment parent;

	@OneToMany(mappedBy = "parent", orphanRemoval = true)
	private List<Comment> children = new ArrayList<>();

	@Column(name = "is_deleted")
	private Boolean isDeleted;

	public Comment(String content) {
		this.content = content;
	}

	public static Comment createComment(String content, User user, Long memoryId, Comment parent) {

		return Comment.builder().content(content).user(user).memoryId(memoryId).parent(parent).build();
	}

	@Builder
	public Comment(String content, User user, Long memoryId, Comment parent) {
		this.content = content;
		this.user = user;
		this.memoryId = memoryId;
		this.parent = parent;
	}

	public void delete() {
		this.isDeleted = Boolean.TRUE;
	}

	private void setParent(Comment parent) {
		this.parent = parent;
		if (parent != null) {
			parent.getChildren().add(this);
		}
	}

	@PrePersist
	void init() {
		this.isDeleted = Boolean.FALSE;
	}

	public void update(String content) {
		this.content = content;
	}

	public void remove() {
		this.content = REMOVE_COMMENT;
	}

}
