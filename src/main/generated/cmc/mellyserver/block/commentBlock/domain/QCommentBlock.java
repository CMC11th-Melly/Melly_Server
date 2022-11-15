package cmc.mellyserver.block.commentBlock.domain;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QCommentBlock is a Querydsl query type for CommentBlock
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QCommentBlock extends EntityPathBase<CommentBlock> {

    private static final long serialVersionUID = 2067038808L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QCommentBlock commentBlock = new QCommentBlock("commentBlock");

    public final cmc.mellyserver.common.util.jpa.QJpaBaseEntity _super = new cmc.mellyserver.common.util.jpa.QJpaBaseEntity(this);

    public final NumberPath<Long> blockId = createNumber("blockId", Long.class);

    public final cmc.mellyserver.comment.domain.QComment comment;

    //inherited
    public final DateTimePath<java.time.LocalDateTime> createdDate = _super.createdDate;

    //inherited
    public final DateTimePath<java.time.LocalDateTime> lastModifiedDate = _super.lastModifiedDate;

    public final cmc.mellyserver.user.domain.QUser user;

    public QCommentBlock(String variable) {
        this(CommentBlock.class, forVariable(variable), INITS);
    }

    public QCommentBlock(Path<? extends CommentBlock> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QCommentBlock(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QCommentBlock(PathMetadata metadata, PathInits inits) {
        this(CommentBlock.class, metadata, inits);
    }

    public QCommentBlock(Class<? extends CommentBlock> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.comment = inits.isInitialized("comment") ? new cmc.mellyserver.comment.domain.QComment(forProperty("comment"), inits.get("comment")) : null;
        this.user = inits.isInitialized("user") ? new cmc.mellyserver.user.domain.QUser(forProperty("user"), inits.get("user")) : null;
    }

}

