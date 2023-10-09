package cmc.mellyserver.dbcore.group;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;


/**
 * QUserGroup is a Querydsl query type for UserGroup
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QUserGroup extends EntityPathBase<UserGroup> {

    private static final long serialVersionUID = -1643994381L;

    public static final QUserGroup userGroup = new QUserGroup("userGroup");

    public final cmc.mellyserver.dbcore.config.jpa.QJpaBaseEntity _super = new cmc.mellyserver.dbcore.config.jpa.QJpaBaseEntity(this);

    //inherited
    public final DateTimePath<java.time.LocalDateTime> createdDate = _super.createdDate;

    public final NumberPath<Integer> groupIcon = createNumber("groupIcon", Integer.class);

    public final StringPath groupName = createString("groupName");

    public final EnumPath<cmc.mellyserver.dbcore.group.enums.GroupType> groupType = createEnum("groupType", cmc.mellyserver.dbcore.group.enums.GroupType.class);

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final StringPath inviteLink = createString("inviteLink");

    public final BooleanPath isDeleted = createBoolean("isDeleted");

    //inherited
    public final DateTimePath<java.time.LocalDateTime> lastModifiedDate = _super.lastModifiedDate;

    public final NumberPath<Long> version = createNumber("version", Long.class);

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

