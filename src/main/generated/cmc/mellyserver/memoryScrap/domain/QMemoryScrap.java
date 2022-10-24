package cmc.mellyserver.memoryScrap.domain;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QMemoryScrap is a Querydsl query type for MemoryScrap
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QMemoryScrap extends EntityPathBase<MemoryScrap> {

    private static final long serialVersionUID = -1041348989L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QMemoryScrap memoryScrap = new QMemoryScrap("memoryScrap");

    public final cmc.mellyserver.common.util.jpa.QJpaBaseEntity _super = new cmc.mellyserver.common.util.jpa.QJpaBaseEntity(this);

    //inherited
    public final DateTimePath<java.time.LocalDateTime> createdDate = _super.createdDate;

    public final NumberPath<Long> id = createNumber("id", Long.class);

    //inherited
    public final DateTimePath<java.time.LocalDateTime> lastModifiedDate = _super.lastModifiedDate;

    public final cmc.mellyserver.memory.domain.QMemory memory;

    public final cmc.mellyserver.user.domain.QUser user;

    public QMemoryScrap(String variable) {
        this(MemoryScrap.class, forVariable(variable), INITS);
    }

    public QMemoryScrap(Path<? extends MemoryScrap> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QMemoryScrap(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QMemoryScrap(PathMetadata metadata, PathInits inits) {
        this(MemoryScrap.class, metadata, inits);
    }

    public QMemoryScrap(Class<? extends MemoryScrap> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.memory = inits.isInitialized("memory") ? new cmc.mellyserver.memory.domain.QMemory(forProperty("memory"), inits.get("memory")) : null;
        this.user = inits.isInitialized("user") ? new cmc.mellyserver.user.domain.QUser(forProperty("user"), inits.get("user")) : null;
    }

}

