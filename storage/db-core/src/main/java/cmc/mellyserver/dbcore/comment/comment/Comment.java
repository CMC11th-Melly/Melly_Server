package cmc.mellyserver.dbcore.comment.comment;

import java.time.LocalDateTime;
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
import jakarta.persistence.OneToOne;
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

	@Column(name = "content")
	@Lob
	private String content;

	@ManyToOne
	@JoinColumn(name = "user_id")
	private User user;

	@OneToOne
	@JoinColumn(name = "mention_user_id")
	private User mentionUser;

	@Column(name = "memory_id")
	private Long memoryId;

	@OneToMany(mappedBy = "comment", fetch = FetchType.LAZY, cascade = CascadeType.REMOVE, orphanRemoval = true)
	private List<CommentLike> commentLikes = new ArrayList<>();

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "root_id")
	private Comment root;

	@OneToMany(mappedBy = "root", orphanRemoval = true)
	private List<Comment> children = new ArrayList<>();

	@Column(name = "deleted_at")
	private LocalDateTime deletedAt;

	public Comment(String content) {
		this.content = content;
	}

	public static Comment createRoot(String content, User user, Long memoryId, Comment root) {

		return Comment.builder().content(content).user(user).memoryId(memoryId).root(root).build();
	}

	public static Comment createChild(String content, User user, User mentionUser, Long memoryId, Comment root) {
		return Comment.builder()
			.content(content)
			.user(user)
			.mentionUser(mentionUser)
			.memoryId(memoryId)
			.root(root)
			.build();
	}

	@Builder
	public Comment(String content, User user, User mentionUser, Long memoryId, Comment root) {
		this.content = content;
		this.user = user;
		this.memoryId = memoryId;
		this.mentionUser = mentionUser;
		setRoot(root);
	}

	public void delete() {
		this.deletedAt = LocalDateTime.now();
	}

	private void setRoot(Comment root) {
		this.root = root;
		if (root != null) {
			root.getChildren().add(this);
		}
	}

	public void update(String content) {
		this.content = content;
	}
}
