package cmc.mellyserver.dbcore.comment.comment;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QComment is a Querydsl query type for Comment
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QComment extends EntityPathBase<Comment> {

    private static final long serialVersionUID = -1155202961L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QComment comment = new QComment("comment");

    public final cmc.mellyserver.dbcore.config.jpa.QJpaBaseEntity _super = new cmc.mellyserver.dbcore.config.jpa.QJpaBaseEntity(this);

    public final ListPath<Comment, QComment> children = this.<Comment, QComment>createList("children", Comment.class, QComment.class, PathInits.DIRECT2);

    public final ListPath<cmc.mellyserver.dbcore.comment.commenlike.CommentLike, cmc.mellyserver.dbcore.comment.commenlike.QCommentLike> commentLikes = this.<cmc.mellyserver.dbcore.comment.commenlike.CommentLike, cmc.mellyserver.dbcore.comment.commenlike.QCommentLike>createList("commentLikes", cmc.mellyserver.dbcore.comment.commenlike.CommentLike.class, cmc.mellyserver.dbcore.comment.commenlike.QCommentLike.class, PathInits.DIRECT2);

    public final StringPath content = createString("content");

    //inherited
    public final DateTimePath<java.time.LocalDateTime> createdDate = _super.createdDate;

    public final DateTimePath<java.time.LocalDateTime> deletedAt = createDateTime("deletedAt", java.time.LocalDateTime.class);

    public final NumberPath<Long> id = createNumber("id", Long.class);

    //inherited
    public final DateTimePath<java.time.LocalDateTime> lastModifiedDate = _super.lastModifiedDate;

    public final NumberPath<Long> memoryId = createNumber("memoryId", Long.class);

    public final cmc.mellyserver.dbcore.user.QUser mentionUser;

    public final QComment root;

    public final cmc.mellyserver.dbcore.user.QUser user;

    public QComment(String variable) {
        this(Comment.class, forVariable(variable), INITS);
    }

    public QComment(Path<? extends Comment> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QComment(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QComment(PathMetadata metadata, PathInits inits) {
        this(Comment.class, metadata, inits);
    }

    public QComment(Class<? extends Comment> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.mentionUser = inits.isInitialized("mentionUser") ? new cmc.mellyserver.dbcore.user.QUser(forProperty("mentionUser")) : null;
        this.root = inits.isInitialized("root") ? new QComment(forProperty("root"), inits.get("root")) : null;
        this.user = inits.isInitialized("user") ? new cmc.mellyserver.dbcore.user.QUser(forProperty("user")) : null;
    }

}

