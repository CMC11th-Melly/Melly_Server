package cmc.mellyserver.dbcore.memory;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QMemory is a Querydsl query type for Memory
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QMemory extends EntityPathBase<Memory> {

    private static final long serialVersionUID = 2028643452L;

    public static final QMemory memory = new QMemory("memory");

    public final cmc.mellyserver.dbcore.config.jpa.QJpaBaseEntity _super = new cmc.mellyserver.dbcore.config.jpa.QJpaBaseEntity(this);

    public final StringPath content = createString("content");

    //inherited
    public final DateTimePath<java.time.LocalDateTime> createdDate = _super.createdDate;

    public final DateTimePath<java.time.LocalDateTime> deletedAt = createDateTime("deletedAt", java.time.LocalDateTime.class);

    public final NumberPath<Long> groupId = createNumber("groupId", Long.class);

    public final NumberPath<Long> id = createNumber("id", Long.class);

    //inherited
    public final DateTimePath<java.time.LocalDateTime> lastModifiedDate = _super.lastModifiedDate;

    public final ListPath<MemoryImage, QMemoryImage> memoryImages = this.<MemoryImage, QMemoryImage>createList("memoryImages", MemoryImage.class, QMemoryImage.class, PathInits.DIRECT2);

    public final EnumPath<OpenType> openType = createEnum("openType", OpenType.class);

    public final NumberPath<Long> placeId = createNumber("placeId", Long.class);

    public final NumberPath<Long> stars = createNumber("stars", Long.class);

    public final StringPath title = createString("title");

    public final NumberPath<Long> userId = createNumber("userId", Long.class);

    public final DatePath<java.time.LocalDate> visitedDate = createDate("visitedDate", java.time.LocalDate.class);

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

