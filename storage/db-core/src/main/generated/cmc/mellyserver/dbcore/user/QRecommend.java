package cmc.mellyserver.dbcore.user;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;


/**
 * QRecommend is a Querydsl query type for Recommend
 */
@Generated("com.querydsl.codegen.DefaultEmbeddableSerializer")
public class QRecommend extends BeanPath<Recommend> {

    private static final long serialVersionUID = -1276272085L;

    public static final QRecommend recommend = new QRecommend("recommend");

    public final EnumPath<cmc.mellyserver.dbcore.user.enums.RecommendActivity> recommendActivity = createEnum("recommendActivity", cmc.mellyserver.dbcore.user.enums.RecommendActivity.class);

    public final EnumPath<cmc.mellyserver.dbcore.user.enums.RecommendGroup> recommendGroup = createEnum("recommendGroup", cmc.mellyserver.dbcore.user.enums.RecommendGroup.class);

    public final EnumPath<cmc.mellyserver.dbcore.user.enums.RecommendPlace> recommendPlace = createEnum("recommendPlace", cmc.mellyserver.dbcore.user.enums.RecommendPlace.class);

    public QRecommend(String variable) {
        super(Recommend.class, forVariable(variable));
    }

    public QRecommend(Path<? extends Recommend> path) {
        super(path.getType(), path.getMetadata());
    }

    public QRecommend(PathMetadata metadata) {
        super(Recommend.class, metadata);
    }

}

