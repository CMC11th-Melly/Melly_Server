package cmc.mellyserver.report.commentReport.domain;

import cmc.mellyserver.comment.domain.Comment;
import cmc.mellyserver.common.util.jpa.JpaBaseEntity;
import cmc.mellyserver.memory.domain.Memory;
import cmc.mellyserver.user.domain.User;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class CommentReport extends JpaBaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long reportId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_seq")
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "comment_id")
    private Comment comment;

    private String content;



    public CommentReport(User user, Comment comment,String content)
    {
        this.user = user;
        user.getCommentReports().add(this);
        this.comment = comment;
        comment.getCommentReports().add(this);
        this.content = content;
    }

}
