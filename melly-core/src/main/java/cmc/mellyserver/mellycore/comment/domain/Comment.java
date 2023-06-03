package cmc.mellyserver.mellycore.comment.domain;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import cmc.mellyserver.mellycore.common.enums.DeleteStatus;
import cmc.mellyserver.mellycore.common.util.jpa.JpaBaseEntity;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@Table(name = "tb_comment")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Comment extends JpaBaseEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "comment_id")
	private Long id;

	@Column(nullable = false)
	@Lob
	private String content;

	private Long writerId;

	private Long memoryId;

	private Long metionUser;

	private Long parentId; // 부모 id

	private int step; // depth

	private int childNum; // 자식 댓글 수

	private int ref;

	@OneToMany(mappedBy = "comment", fetch = FetchType.LAZY, cascade = CascadeType.REMOVE, orphanRemoval = true)
	private List<CommentLike> commentLikes = new ArrayList<>();

	//    @ManyToOne(fetch = FetchType.LAZY)
	//    @JoinColumn(name = "parent_id")
	//    private Comment parent;

	//    @OneToMany(mappedBy = "parent",orphanRemoval = true)
	//    private List<Comment> children = new ArrayList<>();

	@Enumerated(value = EnumType.STRING)
	private DeleteStatus isDeleted;

	private boolean isReported = false;

	public Comment(String content) {
		this.content = content;
	}

	public static Comment createComment(String content, Long writerId, Long memoryId, Long parentId, Long mentionUser) {
		Comment comment = new Comment(content);
		comment.memoryId = memoryId;
		comment.writerId = writerId;
		comment.parentId = parentId;
		comment.isDeleted = DeleteStatus.N;
		comment.setMentionUser(mentionUser);
		return comment;
	}

	public void setIsReported(boolean isReported) {
		this.isReported = isReported;
	}

	public void changeDeletedStatus(DeleteStatus deleteStatus) {
		this.isDeleted = deleteStatus;
	}

	//    private void setParent(Comment parent)
	//    {
	//        this.parent = parent;
	//        if(parent != null)
	//        {
	//            parent.getChildren().add(this);
	//        }
	//
	//    }

	public void updateComment(String content) {
		if (content != null) {
			this.content = content;
		}
	}

	private void setMentionUser(Long mentionUser) {
		this.metionUser = mentionUser;
	}

}
