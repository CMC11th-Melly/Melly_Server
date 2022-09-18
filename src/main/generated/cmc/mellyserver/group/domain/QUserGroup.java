package cmc.mellyserver.group.domain;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QUserGroup is a Querydsl query type for UserGroup
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QUserGroup extends EntityPathBase<UserGroup> {

    private static final long serialVersionUID = 874035544L;

    public static final QUserGroup userGroup = new QUserGroup("userGroup");

    public final cmc.mellyserver.common.util.QJpaBaseEntity _super = new cmc.mellyserver.common.util.QJpaBaseEntity(this);

    //inherited
    public final DateTimePath<java.time.LocalDateTime> createdDate = _super.createdDate;

    public final EnumPath<GroupType> groupType = createEnum("groupType", GroupType.class);

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final StringPath inviteLink = createString("inviteLink");

    //inherited
    public final DateTimePath<java.time.LocalDateTime> lastModifiedDate = _super.lastModifiedDate;

    public final ListPath<cmc.mellyserver.user.domain.User, cmc.mellyserver.user.domain.QUser> user = this.<cmc.mellyserver.user.domain.User, cmc.mellyserver.user.domain.QUser>createList("user", cmc.mellyserver.user.domain.User.class, cmc.mellyserver.user.domain.QUser.class, PathInits.DIRECT2);

    public QUserGroup(String variable) {
        super(UserGroup.class, forVariable(variable));
    }

    public QUserGroup(Path<? extends UserGroup> path) {
        super(path.getType(), path.getMetadata());
    }

    public QUserGroup(PathMetadata metadata) {
        super(UserGroup.class, metadata);
    }

}

