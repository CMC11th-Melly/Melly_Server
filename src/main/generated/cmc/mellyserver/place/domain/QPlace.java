package cmc.mellyserver.place.domain;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QPlace is a Querydsl query type for Place
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QPlace extends EntityPathBase<Place> {

    private static final long serialVersionUID = 1889656163L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QPlace place = new QPlace("place");

    public final cmc.mellyserver.common.util.jpa.QJpaBaseEntity _super = new cmc.mellyserver.common.util.jpa.QJpaBaseEntity(this);

    //inherited
    public final DateTimePath<java.time.LocalDateTime> createdDate = _super.createdDate;

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final BooleanPath isScraped = createBoolean("isScraped");

    //inherited
    public final DateTimePath<java.time.LocalDateTime> lastModifiedDate = _super.lastModifiedDate;

    public final ListPath<cmc.mellyserver.memory.domain.Memory, cmc.mellyserver.memory.domain.QMemory> memories = this.<cmc.mellyserver.memory.domain.Memory, cmc.mellyserver.memory.domain.QMemory>createList("memories", cmc.mellyserver.memory.domain.Memory.class, cmc.mellyserver.memory.domain.QMemory.class, PathInits.DIRECT2);

    public final StringPath placeCategory = createString("placeCategory");

    public final StringPath placeImage = createString("placeImage");

    public final StringPath placeName = createString("placeName");

    public final QPosition position;

    public final ListPath<cmc.mellyserver.placeScrap.domain.PlaceScrap, cmc.mellyserver.placeScrap.domain.QPlaceScrap> scraps = this.<cmc.mellyserver.placeScrap.domain.PlaceScrap, cmc.mellyserver.placeScrap.domain.QPlaceScrap>createList("scraps", cmc.mellyserver.placeScrap.domain.PlaceScrap.class, cmc.mellyserver.placeScrap.domain.QPlaceScrap.class, PathInits.DIRECT2);

    public QPlace(String variable) {
        this(Place.class, forVariable(variable), INITS);
    }

    public QPlace(Path<? extends Place> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QPlace(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QPlace(PathMetadata metadata, PathInits inits) {
        this(Place.class, metadata, inits);
    }

    public QPlace(Class<? extends Place> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.position = inits.isInitialized("position") ? new QPosition(forProperty("position")) : null;
    }

}

