package cmc.mellyserver.dbcore.comment.commenlike;

import cmc.mellyserver.dbcore.comment.comment.Comment;
import jakarta.persistence.*;
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

    private Long userId;

    public static CommentLike createCommentLike(Long id, Comment comment) {
        CommentLike commentLike = new CommentLike();
        commentLike.setComment(comment);
        commentLike.userId = id;
        return commentLike;
    }

    public void setComment(Comment comment) {
        this.comment = comment;
    }

}
