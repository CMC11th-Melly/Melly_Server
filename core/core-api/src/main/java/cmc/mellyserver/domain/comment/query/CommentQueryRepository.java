package cmc.mellyserver.domain.comment.query;

import cmc.mellyserver.dbcore.comment.comment.Comment;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

import static cmc.mellyserver.dbcore.comment.comment.QComment.comment;
import static cmc.mellyserver.dbcore.user.QUser.user;


@Repository
@RequiredArgsConstructor
public class CommentQueryRepository {

    private final JPAQueryFactory query;


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
