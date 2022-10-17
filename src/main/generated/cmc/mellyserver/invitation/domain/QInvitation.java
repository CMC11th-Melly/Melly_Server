package cmc.mellyserver.invitation.domain;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;


/**
 * QInvitation is a Querydsl query type for Invitation
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QInvitation extends EntityPathBase<Invitation> {

    private static final long serialVersionUID = -1183783443L;

    public static final QInvitation invitation = new QInvitation("invitation");

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final StringPath inviteEmail = createString("inviteEmail");

    public final NumberPath<Long> issuerId = createNumber("issuerId", Long.class);

    public final BooleanPath isUsed = createBoolean("isUsed");

    public QInvitation(String variable) {
        super(Invitation.class, forVariable(variable));
    }

    public QInvitation(Path<? extends Invitation> path) {
        super(path.getType(), path.getMetadata());
    }

    public QInvitation(PathMetadata metadata) {
        super(Invitation.class, metadata);
    }

}

