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

    public final cmc.mellyserver.common.util.jpa.QJpaBaseEntity _super = new cmc.mellyserver.common.util.jpa.QJpaBaseEntity(this);

    public final EnumPath<cmc.mellyserver.user.domain.enums.AgeGroup> ageGroup = createEnum("ageGroup", cmc.mellyserver.user.domain.enums.AgeGroup.class);

    public final ListPath<cmc.mellyserver.block.commentBlock.domain.CommentBlock, cmc.mellyserver.block.commentBlock.domain.QCommentBlock> commentBlocks = this.<cmc.mellyserver.block.commentBlock.domain.CommentBlock, cmc.mellyserver.block.commentBlock.domain.QCommentBlock>createList("commentBlocks", cmc.mellyserver.block.commentBlock.domain.CommentBlock.class, cmc.mellyserver.block.commentBlock.domain.QCommentBlock.class, PathInits.DIRECT2);

    public final ListPath<cmc.mellyserver.comment.domain.CommentLike, cmc.mellyserver.comment.domain.QCommentLike> commentLikes = this.<cmc.mellyserver.comment.domain.CommentLike, cmc.mellyserver.comment.domain.QCommentLike>createList("commentLikes", cmc.mellyserver.comment.domain.CommentLike.class, cmc.mellyserver.comment.domain.QCommentLike.class, PathInits.DIRECT2);

    public final ListPath<cmc.mellyserver.report.commentReport.domain.CommentReport, cmc.mellyserver.report.commentReport.domain.QCommentReport> commentReports = this.<cmc.mellyserver.report.commentReport.domain.CommentReport, cmc.mellyserver.report.commentReport.domain.QCommentReport>createList("commentReports", cmc.mellyserver.report.commentReport.domain.CommentReport.class, cmc.mellyserver.report.commentReport.domain.QCommentReport.class, PathInits.DIRECT2);

    public final ListPath<cmc.mellyserver.comment.domain.Comment, cmc.mellyserver.comment.domain.QComment> comments = this.<cmc.mellyserver.comment.domain.Comment, cmc.mellyserver.comment.domain.QComment>createList("comments", cmc.mellyserver.comment.domain.Comment.class, cmc.mellyserver.comment.domain.QComment.class, PathInits.DIRECT2);

    //inherited
    public final DateTimePath<java.time.LocalDateTime> createdDate = _super.createdDate;

    public final StringPath email = createString("email");

    public final BooleanPath enableAppPush = createBoolean("enableAppPush");

    public final BooleanPath enableComment = createBoolean("enableComment");

    public final BooleanPath enableCommentLike = createBoolean("enableCommentLike");

    public final StringPath fcmToken = createString("fcmToken");

    public final EnumPath<cmc.mellyserver.user.domain.enums.Gender> gender = createEnum("gender", cmc.mellyserver.user.domain.enums.Gender.class);

    public final ListPath<cmc.mellyserver.group.domain.GroupAndUser, cmc.mellyserver.group.domain.QGroupAndUser> groupAndUsers = this.<cmc.mellyserver.group.domain.GroupAndUser, cmc.mellyserver.group.domain.QGroupAndUser>createList("groupAndUsers", cmc.mellyserver.group.domain.GroupAndUser.class, cmc.mellyserver.group.domain.QGroupAndUser.class, PathInits.DIRECT2);

    //inherited
    public final DateTimePath<java.time.LocalDateTime> lastModifiedDate = _super.lastModifiedDate;

    public final ListPath<cmc.mellyserver.memory.domain.Memory, cmc.mellyserver.memory.domain.QMemory> memories = this.<cmc.mellyserver.memory.domain.Memory, cmc.mellyserver.memory.domain.QMemory>createList("memories", cmc.mellyserver.memory.domain.Memory.class, cmc.mellyserver.memory.domain.QMemory.class, PathInits.DIRECT2);

    public final ListPath<cmc.mellyserver.block.memoryBlock.domain.MemoryBlock, cmc.mellyserver.block.memoryBlock.domain.QMemoryBlock> memoryBlocks = this.<cmc.mellyserver.block.memoryBlock.domain.MemoryBlock, cmc.mellyserver.block.memoryBlock.domain.QMemoryBlock>createList("memoryBlocks", cmc.mellyserver.block.memoryBlock.domain.MemoryBlock.class, cmc.mellyserver.block.memoryBlock.domain.QMemoryBlock.class, PathInits.DIRECT2);

    public final ListPath<cmc.mellyserver.report.memoryReport.domain.MemoryReport, cmc.mellyserver.report.memoryReport.domain.QMemoryReport> memoryReports = this.<cmc.mellyserver.report.memoryReport.domain.MemoryReport, cmc.mellyserver.report.memoryReport.domain.QMemoryReport>createList("memoryReports", cmc.mellyserver.report.memoryReport.domain.MemoryReport.class, cmc.mellyserver.report.memoryReport.domain.QMemoryReport.class, PathInits.DIRECT2);

    public final ListPath<cmc.mellyserver.memoryScrap.domain.MemoryScrap, cmc.mellyserver.memoryScrap.domain.QMemoryScrap> memoryScraps = this.<cmc.mellyserver.memoryScrap.domain.MemoryScrap, cmc.mellyserver.memoryScrap.domain.QMemoryScrap>createList("memoryScraps", cmc.mellyserver.memoryScrap.domain.MemoryScrap.class, cmc.mellyserver.memoryScrap.domain.QMemoryScrap.class, PathInits.DIRECT2);

    public final StringPath nickname = createString("nickname");

    public final ListPath<cmc.mellyserver.notification.domain.Notification, cmc.mellyserver.notification.domain.QNotification> notifications = this.<cmc.mellyserver.notification.domain.Notification, cmc.mellyserver.notification.domain.QNotification>createList("notifications", cmc.mellyserver.notification.domain.Notification.class, cmc.mellyserver.notification.domain.QNotification.class, PathInits.DIRECT2);

    public final StringPath password = createString("password");

    public final ListPath<cmc.mellyserver.placeScrap.domain.PlaceScrap, cmc.mellyserver.placeScrap.domain.QPlaceScrap> placeScraps = this.<cmc.mellyserver.placeScrap.domain.PlaceScrap, cmc.mellyserver.placeScrap.domain.QPlaceScrap>createList("placeScraps", cmc.mellyserver.placeScrap.domain.PlaceScrap.class, cmc.mellyserver.placeScrap.domain.QPlaceScrap.class, PathInits.DIRECT2);

    public final StringPath profileImage = createString("profileImage");

    public final EnumPath<cmc.mellyserver.auth.presentation.dto.Provider> provider = createEnum("provider", cmc.mellyserver.auth.presentation.dto.Provider.class);

    public final QRecommend recommend;

    public final EnumPath<cmc.mellyserver.user.domain.enums.RoleType> roleType = createEnum("roleType", cmc.mellyserver.user.domain.enums.RoleType.class);

    public final NumberPath<Double> storeCapacity = createNumber("storeCapacity", Double.class);

    public final StringPath userId = createString("userId");

    public final NumberPath<Long> userSeq = createNumber("userSeq", Long.class);

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

