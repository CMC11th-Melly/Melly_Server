package cmc.mellyserver.memory.domain;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QMemory is a Querydsl query type for Memory
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QMemory extends EntityPathBase<Memory> {

    private static final long serialVersionUID = -1699894211L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QMemory memory = new QMemory("memory");

    public final cmc.mellyserver.common.util.jpa.QJpaBaseEntity _super = new cmc.mellyserver.common.util.jpa.QJpaBaseEntity(this);

    public final ListPath<cmc.mellyserver.comment.domain.Comment, cmc.mellyserver.comment.domain.QComment> comments = this.<cmc.mellyserver.comment.domain.Comment, cmc.mellyserver.comment.domain.QComment>createList("comments", cmc.mellyserver.comment.domain.Comment.class, cmc.mellyserver.comment.domain.QComment.class, PathInits.DIRECT2);

    public final StringPath content = createString("content");

    //inherited
    public final DateTimePath<java.time.LocalDateTime> createdDate = _super.createdDate;

    public final QGroupInfo groupInfo;

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final BooleanPath isReported = createBoolean("isReported");

    public final ListPath<String, StringPath> keyword = this.<String, StringPath>createList("keyword", String.class, StringPath.class, PathInits.DIRECT2);

    //inherited
    public final DateTimePath<java.time.LocalDateTime> lastModifiedDate = _super.lastModifiedDate;

    public final ListPath<MemoryImage, QMemoryImage> memoryImages = this.<MemoryImage, QMemoryImage>createList("memoryImages", MemoryImage.class, QMemoryImage.class, PathInits.DIRECT2);

    public final ListPath<cmc.mellyserver.report.memoryReport.domain.MemoryReport, cmc.mellyserver.report.memoryReport.domain.QMemoryReport> memoryReports = this.<cmc.mellyserver.report.memoryReport.domain.MemoryReport, cmc.mellyserver.report.memoryReport.domain.QMemoryReport>createList("memoryReports", cmc.mellyserver.report.memoryReport.domain.MemoryReport.class, cmc.mellyserver.report.memoryReport.domain.QMemoryReport.class, PathInits.DIRECT2);

    public final ListPath<cmc.mellyserver.notification.domain.Notification, cmc.mellyserver.notification.domain.QNotification> notifications = this.<cmc.mellyserver.notification.domain.Notification, cmc.mellyserver.notification.domain.QNotification>createList("notifications", cmc.mellyserver.notification.domain.Notification.class, cmc.mellyserver.notification.domain.QNotification.class, PathInits.DIRECT2);

    public final EnumPath<cmc.mellyserver.memory.domain.enums.OpenType> openType = createEnum("openType", cmc.mellyserver.memory.domain.enums.OpenType.class);

    public final cmc.mellyserver.place.domain.QPlace place;

    public final ListPath<cmc.mellyserver.memoryScrap.domain.MemoryScrap, cmc.mellyserver.memoryScrap.domain.QMemoryScrap> scraps = this.<cmc.mellyserver.memoryScrap.domain.MemoryScrap, cmc.mellyserver.memoryScrap.domain.QMemoryScrap>createList("scraps", cmc.mellyserver.memoryScrap.domain.MemoryScrap.class, cmc.mellyserver.memoryScrap.domain.QMemoryScrap.class, PathInits.DIRECT2);

    public final NumberPath<Long> stars = createNumber("stars", Long.class);

    public final StringPath title = createString("title");

    public final cmc.mellyserver.user.domain.QUser user;

    public final DateTimePath<java.time.LocalDateTime> visitedDate = createDateTime("visitedDate", java.time.LocalDateTime.class);

    public QMemory(String variable) {
        this(Memory.class, forVariable(variable), INITS);
    }

    public QMemory(Path<? extends Memory> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QMemory(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QMemory(PathMetadata metadata, PathInits inits) {
        this(Memory.class, metadata, inits);
    }

    public QMemory(Class<? extends Memory> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.groupInfo = inits.isInitialized("groupInfo") ? new QGroupInfo(forProperty("groupInfo")) : null;
        this.place = inits.isInitialized("place") ? new cmc.mellyserver.place.domain.QPlace(forProperty("place"), inits.get("place")) : null;
        this.user = inits.isInitialized("user") ? new cmc.mellyserver.user.domain.QUser(forProperty("user"), inits.get("user")) : null;
    }

}

