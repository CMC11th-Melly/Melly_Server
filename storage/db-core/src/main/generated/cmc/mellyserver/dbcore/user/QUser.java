package cmc.mellyserver.dbcore.user;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;


/**
 * QUser is a Querydsl query type for User
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QUser extends EntityPathBase<User> {

    private static final long serialVersionUID = 1807216892L;

    public static final QUser user = new QUser("user");

    public final cmc.mellyserver.dbcore.config.jpa.QJpaBaseEntity _super = new cmc.mellyserver.dbcore.config.jpa.QJpaBaseEntity(this);

    public final EnumPath<cmc.mellyserver.dbcore.user.enums.AgeGroup> ageGroup = createEnum("ageGroup", cmc.mellyserver.dbcore.user.enums.AgeGroup.class);

    //inherited
    public final DateTimePath<java.time.LocalDateTime> createdDate = _super.createdDate;

    public final StringPath email = createString("email");

    public final BooleanPath enableAppPush = createBoolean("enableAppPush");

    public final BooleanPath enableCommentLikePush = createBoolean("enableCommentLikePush");

    public final BooleanPath enableCommentPush = createBoolean("enableCommentPush");

    public final StringPath fcmToken = createString("fcmToken");

    public final EnumPath<cmc.mellyserver.dbcore.user.enums.Gender> gender = createEnum("gender", cmc.mellyserver.dbcore.user.enums.Gender.class);

    public final NumberPath<Long> id = createNumber("id", Long.class);

    //inherited
    public final DateTimePath<java.time.LocalDateTime> lastModifiedDate = _super.lastModifiedDate;

    public final StringPath nickname = createString("nickname");

    public final StringPath password = createString("password");

    public final StringPath profileImage = createString("profileImage");

    public final EnumPath<cmc.mellyserver.dbcore.user.enums.Provider> provider = createEnum("provider", cmc.mellyserver.dbcore.user.enums.Provider.class);

    public final EnumPath<cmc.mellyserver.dbcore.user.enums.RoleType> roleType = createEnum("roleType", cmc.mellyserver.dbcore.user.enums.RoleType.class);

    public final StringPath socialId = createString("socialId");

    public final EnumPath<cmc.mellyserver.dbcore.user.enums.UserStatus> userStatus = createEnum("userStatus", cmc.mellyserver.dbcore.user.enums.UserStatus.class);

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

