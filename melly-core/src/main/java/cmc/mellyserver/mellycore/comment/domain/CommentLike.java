package cmc.mellyserver.mellycore.comment.domain;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

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
