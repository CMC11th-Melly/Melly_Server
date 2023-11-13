package cmc.mellyserver.dbcore.notification;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;


/**
 * QNotification is a Querydsl query type for Notification
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QNotification extends EntityPathBase<Notification> {

    private static final long serialVersionUID = -176171524L;

    public static final QNotification notification = new QNotification("notification");

    public final cmc.mellyserver.dbcore.config.jpa.QJpaBaseEntity _super = new cmc.mellyserver.dbcore.config.jpa.QJpaBaseEntity(this);

    public final StringPath content = createString("content");

    //inherited
    public final DateTimePath<java.time.LocalDateTime> createdDate = _super.createdDate;

    public final DateTimePath<java.time.LocalDateTime> deletedAt = createDateTime("deletedAt", java.time.LocalDateTime.class);

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final BooleanPath isRead = createBoolean("isRead");

    //inherited
    public final DateTimePath<java.time.LocalDateTime> lastModifiedDate = _super.lastModifiedDate;

    public final NumberPath<Long> memoryId = createNumber("memoryId", Long.class);

    public final StringPath nickname = createString("nickname");

    public final EnumPath<cmc.mellyserver.dbcore.notification.enums.NotificationType> notificationType = createEnum("notificationType", cmc.mellyserver.dbcore.notification.enums.NotificationType.class);

    public final StringPath profileImage = createString("profileImage");

    public final NumberPath<Long> userId = createNumber("userId", Long.class);

    public QNotification(String variable) {
        super(Notification.class, forVariable(variable));
    }

    public QNotification(Path<? extends Notification> path) {
        super(path.getType(), path.getMetadata());
    }

    public QNotification(PathMetadata metadata) {
        super(Notification.class, metadata);
    }

}

