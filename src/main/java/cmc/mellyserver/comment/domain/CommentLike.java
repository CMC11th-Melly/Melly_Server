package cmc.mellyserver.comment.domain;

import cmc.mellyserver.user.domain.User;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.Objects;

@Entity
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

    public void setComment(Comment comment) {
        this.comment = comment;
    }

    public static CommentLike createCommentLike(Long userSeq , Comment comment)
    {
        CommentLike commentLike = new CommentLike();
        commentLike.setComment(comment);
        commentLike.userId = userSeq;
        return commentLike;
    }




}
