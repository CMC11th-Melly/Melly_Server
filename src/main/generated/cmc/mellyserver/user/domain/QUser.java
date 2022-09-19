package cmc.mellyserver.user.domain;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QUser is a Querydsl query type for User
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QUser extends EntityPathBase<User> {

    private static final long serialVersionUID = 815555985L;

    public static final QUser user = new QUser("user");

    public final cmc.mellyserver.common.util.QJpaBaseEntity _super = new cmc.mellyserver.common.util.QJpaBaseEntity(this);

    public final EnumPath<AgeGroup> ageGroup = createEnum("ageGroup", AgeGroup.class);

    //inherited
    public final DateTimePath<java.time.LocalDateTime> createdDate = _super.createdDate;

    public final StringPath email = createString("email");

    public final BooleanPath gender = createBoolean("gender");

    public final ListPath<cmc.mellyserver.group.domain.GroupAndUser, cmc.mellyserver.group.domain.QGroupAndUser> groupAndUsers = this.<cmc.mellyserver.group.domain.GroupAndUser, cmc.mellyserver.group.domain.QGroupAndUser>createList("groupAndUsers", cmc.mellyserver.group.domain.GroupAndUser.class, cmc.mellyserver.group.domain.QGroupAndUser.class, PathInits.DIRECT2);

    //inherited
    public final DateTimePath<java.time.LocalDateTime> lastModifiedDate = _super.lastModifiedDate;

    public final StringPath nickname = createString("nickname");

    public final StringPath password = createString("password");

    public final StringPath profileImage = createString("profileImage");

    public final EnumPath<cmc.mellyserver.auth.presentation.dto.Provider> provider = createEnum("provider", cmc.mellyserver.auth.presentation.dto.Provider.class);

    public final EnumPath<RoleType> roleType = createEnum("roleType", RoleType.class);

    public final StringPath userId = createString("userId");

    public final NumberPath<Long> userSeq = createNumber("userSeq", Long.class);

    public QUser(String variable) {
        super(User.class, forVariable(variable));
    }

    public QUser(Path<? extends User> path) {
        super(path.getType(), path.getMetadata());
    }

    public QUser(PathMetadata metadata) {
        super(User.class, metadata);
    }

}

