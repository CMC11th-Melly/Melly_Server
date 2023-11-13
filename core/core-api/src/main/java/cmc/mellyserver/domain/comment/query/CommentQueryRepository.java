package cmc.mellyserver.domain.comment.query;

import static cmc.mellyserver.dbcore.comment.comment.QComment.*;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.querydsl.jpa.impl.JPAQueryFactory;

import cmc.mellyserver.dbcore.comment.comment.Comment;
import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class CommentQueryRepository {

    private final JPAQueryFactory query;

    public List<Comment> getComments(Long memoryId) {

        return query.selectFrom(comment)
            .where(
                comment.memoryId.eq(memoryId),
                comment.deletedAt.isNull()
            )
            .orderBy(
                comment.root.id.asc().nullsFirst(),
                comment.createdDate.asc())
            .fetch();

    }

}
