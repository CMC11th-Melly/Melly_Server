package cmc.mellyserver.comment.domain;


import cmc.mellyserver.block.commentBlock.domain.CommentBlock;
import cmc.mellyserver.common.util.jpa.JpaBaseEntity;
import cmc.mellyserver.memory.domain.Memory;
import cmc.mellyserver.report.commentReport.domain.CommentReport;
import cmc.mellyserver.user.domain.User;
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

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_seq")
    private User writer;

    private Long metionUser;

//    @ManyToOne(fetch = FetchType.LAZY)
//    @JoinColumn(name = "memory_id")
//    private Memory memory;

    private Long memoryId;

    @OneToMany(mappedBy = "comment",fetch = FetchType.LAZY,cascade = CascadeType.REMOVE, orphanRemoval = true)
    private List<CommentLike> commentLikes = new ArrayList<>();

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "parent_id")
    private Comment parent;


    @OneToMany(mappedBy = "parent",orphanRemoval = true)
    private List<Comment> children = new ArrayList<>();

//    @OneToMany(mappedBy = "comment",fetch = FetchType.LAZY)
//    private List<CommentReport> commentReports = new ArrayList<>();
//
//    @OneToMany(mappedBy = "comment",fetch = FetchType.LAZY)
//    private List<CommentBlock> commentBlocks = new ArrayList<>();

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


    private void setUser(User user)
    {
        this.writer = user;
        user.getComments().add(this);
    }

    private void setMentionUser(Long mentionUser)
    {
        this.metionUser = mentionUser;
    }

    private void setMemory(Memory memory)
    {
        this.memory = memory;
        memory.getComments().add(this);
    }

    private void setParent(Comment parent)
    {
        this.parent = parent;
        if(parent != null)
        {
            parent.getChildren().add(this);
        }

    }

    public static Comment createComment(String content, User writer, Memory memory, Comment parent, Long mentionUser)
    {
        Comment comment = new Comment(content);
        comment.setUser(writer);
        comment.setMemory(memory);
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
