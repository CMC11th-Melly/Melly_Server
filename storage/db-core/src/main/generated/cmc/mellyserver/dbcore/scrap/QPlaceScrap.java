package cmc.mellyserver.dbcore.scrap;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QPlaceScrap is a Querydsl query type for PlaceScrap
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QPlaceScrap extends EntityPathBase<PlaceScrap> {

    private static final long serialVersionUID = 433086681L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QPlaceScrap placeScrap = new QPlaceScrap("placeScrap");

    public final cmc.mellyserver.dbcore.config.jpa.QJpaBaseEntity _super = new cmc.mellyserver.dbcore.config.jpa.QJpaBaseEntity(this);

    //inherited
    public final DateTimePath<java.time.LocalDateTime> createdDate = _super.createdDate;

    public final NumberPath<Long> id = createNumber("id", Long.class);

    //inherited
    public final DateTimePath<java.time.LocalDateTime> lastModifiedDate = _super.lastModifiedDate;

    public final cmc.mellyserver.dbcore.place.QPlace place;

    public final EnumPath<ScrapType> scrapType = createEnum("scrapType", ScrapType.class);

    public final cmc.mellyserver.dbcore.user.QUser user;

    public QPlaceScrap(String variable) {
        this(PlaceScrap.class, forVariable(variable), INITS);
    }

    public QPlaceScrap(Path<? extends PlaceScrap> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QPlaceScrap(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QPlaceScrap(PathMetadata metadata, PathInits inits) {
        this(PlaceScrap.class, metadata, inits);
    }

    public QPlaceScrap(Class<? extends PlaceScrap> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.place = inits.isInitialized("place") ? new cmc.mellyserver.dbcore.place.QPlace(forProperty("place"), inits.get("place")) : null;
        this.user = inits.isInitialized("user") ? new cmc.mellyserver.dbcore.user.QUser(forProperty("user"), inits.get("user")) : null;
    }

}

