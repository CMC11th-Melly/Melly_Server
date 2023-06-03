package cmc.mellyserver.mellycore.comment.domain.repository;

import javax.persistence.EntityManager;

import org.springframework.stereotype.Repository;

import com.querydsl.jpa.impl.JPAQueryFactory;

@Repository
public class CommentQueryRepository {

	private final EntityManager em;
	private final JPAQueryFactory query;

	public CommentQueryRepository(EntityManager em) {
		this.em = em;
		this.query = new JPAQueryFactory(em);
	}

	//    public List<Comment> findComment(Long memoryId) {
	//
	////        return query.selectFrom(comment)
	////                .leftJoin(comment.parent)
	////                .fetchJoin()
	////                .where(
	////                        comment.memoryId.eq(memoryId),
	////                        comment.isReported.eq(false)
	////                      //  commentBlocked(userBlockedCommentId).not()
	////                        )
	////                .orderBy(
	////                        comment.parent.id.asc().nullsFirst(),
	////                        comment.createdDate.asc()
	////                ).fetch();
	//        return null;
	//
	//    }

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
