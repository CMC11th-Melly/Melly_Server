package cmc.mellyserver.dbcore.comment.commenlike;

import cmc.mellyserver.dbcore.comment.comment.Comment;
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
@Entity
@Table(name = "tb_comment_like")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class CommentLike {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "comment_like_id")
    private Long id;

    private Long userId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "comment_id")
    private Comment comment;

    public static CommentLike create(Long userId, Comment comment) {
        CommentLike commentLike = new CommentLike();
        commentLike.addComment(comment);
        commentLike.addUserId(userId);
        return commentLike;
    }

    public void addComment(Comment comment) {
        this.comment = comment;
    }

    public void addUserId(Long userId) {
        this.userId = userId;
    }

}
