package cmc.mellyserver.dbcore.comment.comment;

import java.util.ArrayList;
import java.util.List;

import cmc.mellyserver.dbcore.config.jpa.JpaBaseEntity;
import cmc.mellyserver.dbcore.user.User;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.Lob;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import jakarta.persistence.Version;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "tb_comment")
public class Comment extends JpaBaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "comment_id")
    private Long id;

    @Lob
    @Column(name = "content")
    private String content;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @OneToOne
    @JoinColumn(name = "mention_user_id")
    private User mentionUser;

    @Column(name = "memory_id")
    private Long memoryId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "root_id")
    private Comment root;

    @OneToMany(mappedBy = "root", orphanRemoval = true)
    private List<Comment> children = new ArrayList<>();
    
    @Column(name = "like_count")
    private int likeCount;

    @Enumerated(EnumType.STRING)
    @Column(name = "comment_status")
    private CommentStatus commentStatus;

    @Column(name = "is_deleted")
    private boolean isDeleted;

    @Version
    private Long version;

    public Comment(String content) {
        this.content = content;
    }

    @Builder
    public Comment(String content, User user, User mentionUser, Long memoryId, Comment root) {
        this.content = content;
        this.user = user;
        this.memoryId = memoryId;
        this.mentionUser = mentionUser;
        setRoot(root);
    }

    public static Comment createRoot(String content, User user, Long memoryId) {

        return Comment.builder().content(content).user(user).memoryId(memoryId).build();
    }

    public static Comment createChild(String content, User user, User mentionUser, Long memoryId, Comment root) {
        return Comment.builder()
            .content(content)
            .user(user)
            .mentionUser(mentionUser)
            .memoryId(memoryId)
            .root(root)
            .build();
    }

    public void addLike() {
        this.likeCount += 1;
    }

    public void unLike() {
        this.likeCount -= 1;
    }

    public void delete() {
        this.isDeleted = true;
    }

    private void setRoot(Comment root) {
        this.root = root;
        if (root != null) {
            root.getChildren().add(this);
        }
    }

    public void update(String content) {
        this.content = content;
    }

    @PrePersist
    public void init() {
        this.isDeleted = false;
        this.commentStatus = CommentStatus.ACTIVE;
    }
}
