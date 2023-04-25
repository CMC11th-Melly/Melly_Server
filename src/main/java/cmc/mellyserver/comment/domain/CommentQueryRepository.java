package cmc.mellyserver.comment.domain;

import cmc.mellyserver.user.domain.User;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import java.util.List;
import java.util.stream.Collectors;

import static cmc.mellyserver.comment.domain.QComment.*;
import static cmc.mellyserver.memory.domain.QMemory.memory;

@Repository
public class CommentQueryRepository {

    private final EntityManager em;
    private final JPAQueryFactory query;

    public CommentQueryRepository(EntityManager em) {
        this.em = em;
        this.query = new JPAQueryFactory(em);
    }

    public List<Comment> findComment(Long memoryId, Long userSeq) {

        return query.selectFrom(comment)
                .leftJoin(comment.parent)
                .fetchJoin()
                .where(
                        comment.memoryId.eq(memoryId),
                        comment.isReported.eq(false)
                      //  commentBlocked(userBlockedCommentId).not()
                        )
                .orderBy(
                        comment.parent.id.asc().nullsFirst(),
                        comment.createdDate.asc()
                ).fetch();
    }

//    private BooleanExpression commentBlocked(List<Long> compare) {
//
//        if(compare == null)
//        {
//            return null;
//        }
//
//        return comment.id.in(compare);
//    }
}
