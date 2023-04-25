package cmc.mellyserver.comment.domain;



import cmc.mellyserver.common.enums.DeleteStatus;
import cmc.mellyserver.common.util.jpa.JpaBaseEntity;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Comment extends JpaBaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    @Lob
    private String content;

    private Long writerId;

    private Long memoryId;

    private Long metionUser;


    @OneToMany(mappedBy = "comment",fetch = FetchType.LAZY,cascade = CascadeType.REMOVE, orphanRemoval = true)
    private List<CommentLike> commentLikes = new ArrayList<>();

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "parent_id")
    private Comment parent;

    @OneToMany(mappedBy = "parent",orphanRemoval = true)
    private List<Comment> children = new ArrayList<>();

    @Enumerated(value = EnumType.STRING)
    private DeleteStatus isDeleted;

    private boolean isReported = false;

    public void setIsReported(boolean isReported)
    {
        this.isReported = isReported;
    }

    public void changeDeletedStatus(DeleteStatus deleteStatus) {
        this.isDeleted = deleteStatus;
    }

    public void updateComment(String content)
    {
        if(content != null)
        {
            this.content = content;
        }
    }



    private void setMentionUser(Long mentionUser)
    {
        this.metionUser = mentionUser;
    }

    private void setParent(Comment parent)
    {
        this.parent = parent;
        if(parent != null)
        {
            parent.getChildren().add(this);
        }

    }

    public static Comment createComment(String content, Long writerId, Long memoryId, Comment parent, Long mentionUser)
    {
        Comment comment = new Comment(content);
        comment.memoryId = memoryId;
        comment.writerId = writerId;
        comment.setParent(parent);
        comment.isDeleted = DeleteStatus.N;
        comment.setMentionUser(mentionUser);
        return comment;
    }

    public Comment(String content)
    {
        this.content = content;
    }

}
