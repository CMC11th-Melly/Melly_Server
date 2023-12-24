package cmc.mellyserver.dbcore.group;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QGroupAndUser is a Querydsl query type for GroupAndUser
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QGroupAndUser extends EntityPathBase<GroupAndUser> {

    private static final long serialVersionUID = -1674545948L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QGroupAndUser groupAndUser = new QGroupAndUser("groupAndUser");

    public final cmc.mellyserver.dbcore.config.jpa.QJpaBaseEntity _super = new cmc.mellyserver.dbcore.config.jpa.QJpaBaseEntity(this);

    //inherited
    public final DateTimePath<java.time.LocalDateTime> createdDate = _super.createdDate;

    public final QUserGroup group;

    public final NumberPath<Long> id = createNumber("id", Long.class);

    //inherited
    public final DateTimePath<java.time.LocalDateTime> lastModifiedDate = _super.lastModifiedDate;

    public final NumberPath<Long> userId = createNumber("userId", Long.class);

    public QGroupAndUser(String variable) {
        this(GroupAndUser.class, forVariable(variable), INITS);
    }

    public QGroupAndUser(Path<? extends GroupAndUser> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QGroupAndUser(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QGroupAndUser(PathMetadata metadata, PathInits inits) {
        this(GroupAndUser.class, metadata, inits);
    }

    public QGroupAndUser(Class<? extends GroupAndUser> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.group = inits.isInitialized("group") ? new QUserGroup(forProperty("group")) : null;
    }

}

