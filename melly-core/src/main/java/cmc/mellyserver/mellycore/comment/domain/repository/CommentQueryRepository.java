package cmc.mellyserver.mellycore.comment.domain.repository;

import cmc.mellyserver.mellycore.comment.domain.Comment;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import java.util.List;

import static cmc.mellyserver.mellycore.comment.domain.QComment.comment;
import static cmc.mellyserver.mellycore.comment.domain.QCommentLike.commentLike;
import static cmc.mellyserver.mellycore.user.domain.QUser.user;

@Repository
public class CommentQueryRepository {

    private final EntityManager em;
    private final JPAQueryFactory query;

    public CommentQueryRepository(EntityManager em) {
        this.em = em;
        this.query = new JPAQueryFactory(em);
    }

    public List<Comment> findComment(Long userSeq, Long memoryId) {

        return query.selectFrom(comment)
                .innerJoin(user).on(comment.writerId.eq(user.userSeq)).fetchJoin()
                .innerJoin(commentLike).on(comment.id.eq(commentLike.comment.id)).fetchJoin()
                .innerJoin(comment.parent).fetchJoin()
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
