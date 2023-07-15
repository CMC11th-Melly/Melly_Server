package cmc.mellyserver.mellycore.comment.domain;

import cmc.mellyserver.mellycommon.enums.DeleteStatus;
import cmc.mellyserver.mellycore.common.util.jpa.JpaBaseEntity;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "tb_comment")
public class Comment extends JpaBaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "comment_id")
    private Long id;

    @Column(name = "content", nullable = false)
    @Lob
    private String content;

    @Column(name = "writer_id")
    private Long writerId;

    @Column(name = "memory_id")
    private Long memoryId;

    @OneToMany(mappedBy = "comment", fetch = FetchType.LAZY, cascade = CascadeType.REMOVE, orphanRemoval = true)
    private List<CommentLike> commentLikes = new ArrayList<>();

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "parent_id")
    private Comment parent;

    @OneToMany(mappedBy = "parent", orphanRemoval = true)
    private List<Comment> children = new ArrayList<>();

    @Enumerated(value = EnumType.STRING)
    private DeleteStatus isDeleted;

    public Comment(String content) {
        this.content = content;
    }

    public static Comment createComment(String content, Long writerId, Long memoryId, Comment parent) {

        return Comment.builder().content(content).writerId(writerId).memoryId(memoryId).parent(parent).build();
    }

    @Builder
    public Comment(String content, Long writerId, Long memoryId, Comment parent) {
        this.content = content;
        this.writerId = writerId;
        this.memoryId = memoryId;
        this.parent = parent;
    }


    public void delete() {
        this.isDeleted = DeleteStatus.Y;
    }

    private void setParent(Comment parent) {
        this.parent = parent;
        if (parent != null) {
            parent.getChildren().add(this);
        }
    }

    public void updateComment(String content) {
        this.content = content;
    }

}
