package cmc.mellyserver.user.domain;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;


/**
 * QVisitedPlace is a Querydsl query type for VisitedPlace
 */
@Generated("com.querydsl.codegen.DefaultEmbeddableSerializer")
public class QVisitedPlace extends BeanPath<VisitedPlace> {

    private static final long serialVersionUID = -878349373L;

    public static final QVisitedPlace visitedPlace = new QVisitedPlace("visitedPlace");

    public final StringPath color = createString("color");

    public final NumberPath<Integer> count = createNumber("count", Integer.class);

    public final SimplePath<VisitedPlace.Position> position = createSimple("position", VisitedPlace.Position.class);

    public QVisitedPlace(String variable) {
        super(VisitedPlace.class, forVariable(variable));
    }

    public QVisitedPlace(Path<? extends VisitedPlace> path) {
        super(path.getType(), path.getMetadata());
    }

    public QVisitedPlace(PathMetadata metadata) {
        super(VisitedPlace.class, metadata);
    }

}

