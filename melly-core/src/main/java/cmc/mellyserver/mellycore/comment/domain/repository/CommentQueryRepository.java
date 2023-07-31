package cmc.mellyserver.mellycore.comment.domain.repository;

import cmc.mellyserver.mellycore.comment.domain.Comment;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import java.util.List;

import static cmc.mellyserver.mellycore.comment.domain.QComment.comment;

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
//                .innerJoin(user).on(comment.user.id.eq(userId)).fetchJoin()
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
