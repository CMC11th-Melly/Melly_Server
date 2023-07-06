package cmc.mellyserver.mellycore.comment.domain.repository;

import static cmc.mellyserver.mellycore.comment.domain.QComment.comment;

import cmc.mellyserver.mellycore.comment.domain.Comment;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.util.List;
import javax.persistence.EntityManager;
import org.springframework.stereotype.Repository;

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
                .where(
                        comment.memoryId.eq(memoryId),
                        comment.isReported.eq(false)
                )
                .orderBy(
                        comment.parent.id.asc().nullsFirst(),
                        comment.createdDate.asc()
                ).fetch();

    }
}
