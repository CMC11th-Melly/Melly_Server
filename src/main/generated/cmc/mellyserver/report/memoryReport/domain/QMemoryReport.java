package cmc.mellyserver.report.memoryReport.domain;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QMemoryReport is a Querydsl query type for MemoryReport
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QMemoryReport extends EntityPathBase<MemoryReport> {

    private static final long serialVersionUID = 1199363841L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QMemoryReport memoryReport = new QMemoryReport("memoryReport");

    public final StringPath content = createString("content");

    public final cmc.mellyserver.memory.domain.QMemory memory;

    public final NumberPath<Long> reportId = createNumber("reportId", Long.class);

    public final cmc.mellyserver.user.domain.QUser user;

    public QMemoryReport(String variable) {
        this(MemoryReport.class, forVariable(variable), INITS);
    }

    public QMemoryReport(Path<? extends MemoryReport> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QMemoryReport(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QMemoryReport(PathMetadata metadata, PathInits inits) {
        this(MemoryReport.class, metadata, inits);
    }

    public QMemoryReport(Class<? extends MemoryReport> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.memory = inits.isInitialized("memory") ? new cmc.mellyserver.memory.domain.QMemory(forProperty("memory"), inits.get("memory")) : null;
        this.user = inits.isInitialized("user") ? new cmc.mellyserver.user.domain.QUser(forProperty("user"), inits.get("user")) : null;
    }

}

