package cmc.mellyserver.mellyapi.domain.group.query;

import cmc.mellyserver.mellyapi.domain.group.query.dto.GroupLoginUserParticipatedResponseDto;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.SliceImpl;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import java.util.List;

import static cmc.mellyserver.dbcore.group.QGroupAndUser.groupAndUser;
import static cmc.mellyserver.dbcore.group.QUserGroup.userGroup;
import static cmc.mellyserver.dbcore.user.QUser.user;


@Repository
public class UserGroupQueryRepository {

    private final EntityManager em;

    private final JPAQueryFactory query;

    public UserGroupQueryRepository(EntityManager em) {
        this.em = em;
        this.query = new JPAQueryFactory(em);
    }


    public Slice<GroupLoginUserParticipatedResponseDto> getGroupListLoginUserParticipate(Long userId, Long lastId, Pageable pageable) {

        List<GroupLoginUserParticipatedResponseDto> results = query.select(Projections.constructor(GroupLoginUserParticipatedResponseDto.class, userGroup.id, userGroup.groupIcon, userGroup.groupName, userGroup.groupType))
                .from(groupAndUser)
                .innerJoin(groupAndUser.group, userGroup)
                .innerJoin(groupAndUser.user, user)
                .where(
                        ltGroupId(lastId),
                        eqUser(userId)

                ).orderBy(userGroup.id.desc())
                .limit(pageable.getPageSize() + 1)
                .fetch();

        boolean hasNext = false;

        if (results.size() > pageable.getPageSize()) {
            hasNext = true;
            results.remove(pageable.getPageSize());
        }

        return new SliceImpl<>(results, pageable, hasNext);
    }

    private BooleanExpression ltGroupId(Long groupId) {

        if (groupId == null) {
            return null;
        }

        return userGroup.id.lt(groupId);
    }

    private BooleanExpression eqUser(Long userId) {
        if (userId == null) {
            return null;
        }
        return groupAndUser.user.id.eq(userId);
    }
}
