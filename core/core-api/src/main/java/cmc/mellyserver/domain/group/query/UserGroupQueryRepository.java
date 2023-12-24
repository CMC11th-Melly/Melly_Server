package cmc.mellyserver.domain.group.query;

import static cmc.mellyserver.dbcore.group.QGroupAndUser.*;
import static cmc.mellyserver.dbcore.group.QUserGroup.*;

import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.SliceImpl;
import org.springframework.stereotype.Repository;

import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;

import cmc.mellyserver.domain.group.query.dto.UserJoinedGroupsResponseDto;
import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class UserGroupQueryRepository {

    private final JPAQueryFactory query;

    public Slice<UserJoinedGroupsResponseDto> getGroupListLoginUserParticipate(Long userId, Long lastId,
        Pageable pageable) {

        List<UserJoinedGroupsResponseDto> results = query
            .select(
                Projections.constructor(UserJoinedGroupsResponseDto.class,
                    userGroup.id,
                    userGroup.icon,
                    userGroup.name,
                    userGroup.groupType
                )
            )
            .from(groupAndUser)
            .leftJoin(groupAndUser.group, userGroup)
            .where(
                ltGroupId(lastId),
                eqUser(userId)
            )
            .orderBy(userGroup.id.desc())
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

        if (groupId == -1L) {
            return null;
        }

        return userGroup.id.lt(groupId);
    }

    private BooleanExpression eqUser(Long userId) {
        if (userId == null) {
            return null;
        }
        return groupAndUser.userId.eq(userId);
    }

}
