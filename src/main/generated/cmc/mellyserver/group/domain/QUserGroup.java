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

    public final cmc.mellyserver.common.util.jpa.QJpaBaseEntity _super = new cmc.mellyserver.common.util.jpa.QJpaBaseEntity(this);

    //inherited
    public final DateTimePath<java.time.LocalDateTime> createdDate = _super.createdDate;

    public final ListPath<GroupAndUser, QGroupAndUser> groupAndUsers = this.<GroupAndUser, QGroupAndUser>createList("groupAndUsers", GroupAndUser.class, QGroupAndUser.class, PathInits.DIRECT2);

    public final StringPath groupName = createString("groupName");

    public final EnumPath<cmc.mellyserver.group.domain.enums.GroupType> groupType = createEnum("groupType", cmc.mellyserver.group.domain.enums.GroupType.class);

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final StringPath inviteLink = createString("inviteLink");

    //inherited
    public final DateTimePath<java.time.LocalDateTime> lastModifiedDate = _super.lastModifiedDate;

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

