package cmc.mellyserver.memory.domain;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;


/**
 * QMemory is a Querydsl query type for Memory
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QMemory extends EntityPathBase<Memory> {

    private static final long serialVersionUID = -1699894211L;

    public static final QMemory memory = new QMemory("memory");

    public final cmc.mellyserver.common.util.QJpaBaseEntity _super = new cmc.mellyserver.common.util.QJpaBaseEntity(this);

    //inherited
    public final DateTimePath<java.time.LocalDateTime> createdDate = _super.createdDate;

    public final NumberPath<Long> id = createNumber("id", Long.class);

    //inherited
    public final DateTimePath<java.time.LocalDateTime> lastModifiedDate = _super.lastModifiedDate;

    public final EnumPath<MemoryType> memoryType = createEnum("memoryType", MemoryType.class);

    public final NumberPath<Integer> stars = createNumber("stars", Integer.class);

    public QMemory(String variable) {
        super(Memory.class, forVariable(variable));
    }

    public QMemory(Path<? extends Memory> path) {
        super(path.getType(), path.getMetadata());
    }

    public QMemory(PathMetadata metadata) {
        super(Memory.class, metadata);
    }

}

