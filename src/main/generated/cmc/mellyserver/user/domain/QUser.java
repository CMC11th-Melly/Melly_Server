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

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QUser user = new QUser("user");

    public final cmc.mellyserver.common.util.QJpaBaseEntity _super = new cmc.mellyserver.common.util.QJpaBaseEntity(this);

    public final EnumPath<AgeGroup> ageGroup = createEnum("ageGroup", AgeGroup.class);

    //inherited
    public final DateTimePath<java.time.LocalDateTime> createdDate = _super.createdDate;

    public final StringPath email = createString("email");

    public final EnumPath<Gender> gender = createEnum("gender", Gender.class);

    public final ListPath<cmc.mellyserver.group.domain.GroupAndUser, cmc.mellyserver.group.domain.QGroupAndUser> groupAndUsers = this.<cmc.mellyserver.group.domain.GroupAndUser, cmc.mellyserver.group.domain.QGroupAndUser>createList("groupAndUsers", cmc.mellyserver.group.domain.GroupAndUser.class, cmc.mellyserver.group.domain.QGroupAndUser.class, PathInits.DIRECT2);

    //inherited
    public final DateTimePath<java.time.LocalDateTime> lastModifiedDate = _super.lastModifiedDate;

    public final ListPath<cmc.mellyserver.memory.domain.Memory, cmc.mellyserver.memory.domain.QMemory> memories = this.<cmc.mellyserver.memory.domain.Memory, cmc.mellyserver.memory.domain.QMemory>createList("memories", cmc.mellyserver.memory.domain.Memory.class, cmc.mellyserver.memory.domain.QMemory.class, PathInits.DIRECT2);

    public final StringPath nickname = createString("nickname");

    public final StringPath password = createString("password");

    public final StringPath profileImage = createString("profileImage");

    public final EnumPath<cmc.mellyserver.auth.presentation.dto.Provider> provider = createEnum("provider", cmc.mellyserver.auth.presentation.dto.Provider.class);

    public final QRecommend recommend;

    public final EnumPath<RoleType> roleType = createEnum("roleType", RoleType.class);

    public final ListPath<cmc.mellyserver.place.domain.Scrap, cmc.mellyserver.place.domain.QScrap> scraps = this.<cmc.mellyserver.place.domain.Scrap, cmc.mellyserver.place.domain.QScrap>createList("scraps", cmc.mellyserver.place.domain.Scrap.class, cmc.mellyserver.place.domain.QScrap.class, PathInits.DIRECT2);

    public final NumberPath<Double> storeCapacity = createNumber("storeCapacity", Double.class);

    public final StringPath userId = createString("userId");

    public final NumberPath<Long> userSeq = createNumber("userSeq", Long.class);

    public final SetPath<Long, NumberPath<Long>> visitedPlace = this.<Long, NumberPath<Long>>createSet("visitedPlace", Long.class, NumberPath.class, PathInits.DIRECT2);

    public QUser(String variable) {
        this(User.class, forVariable(variable), INITS);
    }

    public QUser(Path<? extends User> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QUser(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QUser(PathMetadata metadata, PathInits inits) {
        this(User.class, metadata, inits);
    }

    public QUser(Class<? extends User> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.recommend = inits.isInitialized("recommend") ? new QRecommend(forProperty("recommend")) : null;
    }

}

