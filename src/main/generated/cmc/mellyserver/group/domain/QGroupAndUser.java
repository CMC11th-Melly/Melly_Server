package cmc.mellyserver.group.domain;

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

    private static final long serialVersionUID = 1351125087L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QGroupAndUser groupAndUser = new QGroupAndUser("groupAndUser");

    public final QUserGroup group;

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final cmc.mellyserver.user.domain.QUser user;

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
        this.user = inits.isInitialized("user") ? new cmc.mellyserver.user.domain.QUser(forProperty("user"), inits.get("user")) : null;
    }

}

