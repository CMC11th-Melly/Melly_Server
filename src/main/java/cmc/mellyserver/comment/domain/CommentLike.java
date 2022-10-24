package cmc.mellyserver.comment.domain;

import cmc.mellyserver.user.domain.User;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.Objects;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class CommentLike {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "comment_like_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "comment_id")
    private Comment comment;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_seq")
    private User user;

    public void setComment(Comment comment)
    {
        this.comment = comment;
        comment.getCommentLikes().add(this);
    }

    public void setUser(User user)
    {
        this.user = user;
        user.getCommentLikes().add(this);
    }

    public static CommentLike createCommentLike(User user , Comment comment)
    {
        CommentLike commentLike = new CommentLike();
        commentLike.setComment(comment);
        commentLike.setUser(user);
        return commentLike;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CommentLike that = (CommentLike) o;
        return Objects.equals(comment, that.comment) && Objects.equals(user, that.user);
    }

    @Override
    public int hashCode() {
        return Objects.hash(comment, user);
    }

}
