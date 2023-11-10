package cmc.mellyserver.domain.comment.query;

import static cmc.mellyserver.dbcore.comment.comment.QComment.*;
import static cmc.mellyserver.dbcore.user.QUser.*;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.querydsl.jpa.impl.JPAQueryFactory;

import cmc.mellyserver.dbcore.comment.comment.Comment;
import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class CommentQueryRepository {

	private final JPAQueryFactory query;

	public List<Comment> findComment(Long userId, Long memoryId) {

		return query.selectFrom(comment)
			.innerJoin(user)
			.on(user.id.eq(userId))
			.fetchJoin()
			.where(comment.memoryId.eq(memoryId))
			.orderBy(
				comment.parent.id.asc().nullsFirst(),
				comment.createdDate.asc())
			.fetch();

	}

}
