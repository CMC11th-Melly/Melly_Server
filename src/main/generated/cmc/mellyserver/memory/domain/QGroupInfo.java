package cmc.mellyserver.memory.domain;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;


/**
 * QGroupInfo is a Querydsl query type for GroupInfo
 */
@Generated("com.querydsl.codegen.DefaultEmbeddableSerializer")
public class QGroupInfo extends BeanPath<GroupInfo> {

    private static final long serialVersionUID = 1496084241L;

    public static final QGroupInfo groupInfo = new QGroupInfo("groupInfo");

    public final NumberPath<Long> groupId = createNumber("groupId", Long.class);

    public final EnumPath<cmc.mellyserver.group.domain.enums.GroupType> groupType = createEnum("groupType", cmc.mellyserver.group.domain.enums.GroupType.class);

    public QGroupInfo(String variable) {
        super(GroupInfo.class, forVariable(variable));
    }

    public QGroupInfo(Path<? extends GroupInfo> path) {
        super(path.getType(), path.getMetadata());
    }

    public QGroupInfo(PathMetadata metadata) {
        super(GroupInfo.class, metadata);
    }

}

