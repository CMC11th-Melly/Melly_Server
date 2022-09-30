package cmc.mellyserver.memory.domain;

import static com.querydsl.core.types.PathMetadataFactory.*;

import cmc.mellyserver.memory.domain.enums.OpenType;
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

    private static final long serialVersionUID = -1699894211L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QMemory memory = new QMemory("memory");

    public final cmc.mellyserver.common.util.QJpaBaseEntity _super = new cmc.mellyserver.common.util.QJpaBaseEntity(this);

    public final StringPath content = createString("content");

    //inherited
    public final DateTimePath<java.time.LocalDateTime> createdDate = _super.createdDate;

    public final QGroupInfo groupInfo;

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final StringPath keyword = createString("keyword");

    //inherited
    public final DateTimePath<java.time.LocalDateTime> lastModifiedDate = _super.lastModifiedDate;

    public final ListPath<MemoryImage, QMemoryImage> memoryImages = this.<MemoryImage, QMemoryImage>createList("memoryImages", MemoryImage.class, QMemoryImage.class, PathInits.DIRECT2);

    public final EnumPath<OpenType> openType = createEnum("openType", OpenType.class);

    public final cmc.mellyserver.place.domain.QPlace place;

    public final NumberPath<Integer> stars = createNumber("stars", Integer.class);

    public final StringPath title = createString("title");

    public final cmc.mellyserver.user.domain.QUser user;

    public QMemory(String variable) {
        this(Memory.class, forVariable(variable), INITS);
    }

    public QMemory(Path<? extends Memory> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QMemory(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QMemory(PathMetadata metadata, PathInits inits) {
        this(Memory.class, metadata, inits);
    }

    public QMemory(Class<? extends Memory> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.groupInfo = inits.isInitialized("groupInfo") ? new QGroupInfo(forProperty("groupInfo")) : null;
        this.place = inits.isInitialized("place") ? new cmc.mellyserver.place.domain.QPlace(forProperty("place"), inits.get("place")) : null;
        this.user = inits.isInitialized("user") ? new cmc.mellyserver.user.domain.QUser(forProperty("user"), inits.get("user")) : null;
    }

}

