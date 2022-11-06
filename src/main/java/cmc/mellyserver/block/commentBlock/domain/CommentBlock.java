package cmc.mellyserver.block.commentBlock.domain;

import cmc.mellyserver.comment.domain.Comment;
import cmc.mellyserver.user.domain.User;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class CommentBlock {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long blockId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_seq")
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "comment_id")
    private Comment comment;

    public CommentBlock(User user, Comment comment)
    {
        this.user = user;
        user.getCommentBlocks().add(this);
        this.comment = comment;
        comment.getCommentBlocks().add(this);

    }

}
