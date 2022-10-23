package cmc.mellyserver.comment.domain;

import com.querydsl.jpa.impl.JPAQueryFactory;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import java.util.List;

import static cmc.mellyserver.comment.domain.QComment.*;

@Repository
public class CommentQueryRepository {

    private final EntityManager em;
    private final JPAQueryFactory query;

    public CommentQueryRepository(EntityManager em) {
        this.em = em;
        this.query = new JPAQueryFactory(em);
    }

    public List<Comment> findComment(Long memoryId) {
        return query.selectFrom(comment)
                .leftJoin(comment.parent)
                .fetchJoin()
                .where(comment.memory.id.eq(memoryId))
                .orderBy(
                        comment.parent.id.asc().nullsFirst(),
                        comment.createdDate.asc()
                ).fetch();
    }
}
