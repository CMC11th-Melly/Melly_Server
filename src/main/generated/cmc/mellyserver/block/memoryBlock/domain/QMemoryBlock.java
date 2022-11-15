package cmc.mellyserver.block.memoryBlock.domain;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QMemoryBlock is a Querydsl query type for MemoryBlock
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QMemoryBlock extends EntityPathBase<MemoryBlock> {

    private static final long serialVersionUID = -1688287420L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QMemoryBlock memoryBlock = new QMemoryBlock("memoryBlock");

    public final cmc.mellyserver.common.util.jpa.QJpaBaseEntity _super = new cmc.mellyserver.common.util.jpa.QJpaBaseEntity(this);

    public final NumberPath<Long> blockId = createNumber("blockId", Long.class);

    //inherited
    public final DateTimePath<java.time.LocalDateTime> createdDate = _super.createdDate;

    //inherited
    public final DateTimePath<java.time.LocalDateTime> lastModifiedDate = _super.lastModifiedDate;

    public final cmc.mellyserver.memory.domain.QMemory memory;

    public final cmc.mellyserver.user.domain.QUser user;

    public QMemoryBlock(String variable) {
        this(MemoryBlock.class, forVariable(variable), INITS);
    }

    public QMemoryBlock(Path<? extends MemoryBlock> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QMemoryBlock(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QMemoryBlock(PathMetadata metadata, PathInits inits) {
        this(MemoryBlock.class, metadata, inits);
    }

    public QMemoryBlock(Class<? extends MemoryBlock> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.memory = inits.isInitialized("memory") ? new cmc.mellyserver.memory.domain.QMemory(forProperty("memory"), inits.get("memory")) : null;
        this.user = inits.isInitialized("user") ? new cmc.mellyserver.user.domain.QUser(forProperty("user"), inits.get("user")) : null;
    }

}

