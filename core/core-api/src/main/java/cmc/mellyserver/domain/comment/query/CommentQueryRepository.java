package cmc.mellyserver.domain.comment.query;

import cmc.mellyserver.dbcore.comment.comment.Comment;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import java.util.List;

import static cmc.mellyserver.dbcore.comment.comment.QComment.comment;
import static cmc.mellyserver.dbcore.user.QUser.user;


@Repository
public class CommentQueryRepository {

    private final EntityManager em;
    private final JPAQueryFactory query;

    public CommentQueryRepository(EntityManager em) {
        this.em = em;
        this.query = new JPAQueryFactory(em);
    }

    public List<Comment> findComment(Long userId, Long memoryId) {

        return query.selectFrom(comment)
                .innerJoin(user).on(user.id.eq(userId)).fetchJoin()
//                .innerJoin(commentLike).on(comment.id.eq(commentLike.comment.id)).fetchJoin()
                .where(
                        comment.memoryId.eq(memoryId)
                )
                .orderBy(
//                        comment.parent.id.asc().nullsFirst(),
                        comment.createdDate.asc()
                ).fetch();

    }
}
