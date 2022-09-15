package cmc.mellyserver.user.domain;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QMember is a Querydsl query type for Member
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QMember extends EntityPathBase<User> {

    private static final long serialVersionUID = -1734015057L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QMember member = new QMember("member1");

    public final DateTimePath<java.time.LocalDateTime> birthday = createDateTime("birthday", java.time.LocalDateTime.class);

    public final StringPath email = createString("email");

    public final BooleanPath gender = createBoolean("gender");

    public final cmc.mellyserver.group.domain.QGroup group;

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final StringPath nickname = createString("nickname");

    public final StringPath password = createString("password");

    public final StringPath profileImage = createString("profileImage");

    public final EnumPath<RoleType> roleType = createEnum("roleType", RoleType.class);

    public final StringPath socialId = createString("socialId");

    public final QVisitedPlace visitedPlace;

    public QMember(String variable) {
        this(User.class, forVariable(variable), INITS);
    }

    public QMember(Path<? extends User> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QMember(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QMember(PathMetadata metadata, PathInits inits) {
        this(User.class, metadata, inits);
    }

    public QMember(Class<? extends User> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.group = inits.isInitialized("group") ? new cmc.mellyserver.group.domain.QGroup(forProperty("group")) : null;
        this.visitedPlace = inits.isInitialized("visitedPlace") ? new QVisitedPlace(forProperty("visitedPlace")) : null;
    }

}

