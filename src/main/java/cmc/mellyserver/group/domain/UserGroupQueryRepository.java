package cmc.mellyserver.group.domain;

import cmc.mellyserver.common.util.jpa.QueryDslUtil;
import cmc.mellyserver.group.domain.enums.GroupType;
import cmc.mellyserver.memory.domain.Memory;
import cmc.mellyserver.memory.domain.enums.OpenType;
import cmc.mellyserver.user.domain.User;
import com.querydsl.core.types.Order;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.Predicate;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.JPAExpressions;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.SliceImpl;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static cmc.mellyserver.group.domain.QGroupAndUser.groupAndUser;
import static cmc.mellyserver.group.domain.QUserGroup.userGroup;
import static cmc.mellyserver.memory.domain.QMemory.memory;
import static cmc.mellyserver.user.domain.QUser.user;
import static org.springframework.util.ObjectUtils.isEmpty;

@Repository
public class UserGroupQueryRepository {


    private final EntityManager em;
    private final JPAQueryFactory query;

    // 인텔리제이 인식 오류, 무시하고 진행하면 된다.
    public UserGroupQueryRepository(EntityManager em)
    {
        this.em = em;
        this.query = new JPAQueryFactory(em);
    }





    public Slice<Memory> getMyGroupMemory(Pageable pageable, Long groupId,User loginUser,Long userSeq) {

        List<Long> userBlockedMemoryId = loginUser.getMemoryBlocks().stream().map(m -> m.getMemory().getId()).collect(Collectors.toList());
        List<OrderSpecifier> ORDERS = getAllOrderSpecifiers(pageable);

        // 1. 메모리를 가져올꺼야
        List<Memory> results = query.select(memory)
                .from(userGroup)
                // 2. 메모리와 그 메모리를 가진 유저를 조인
                .join(userGroup.groupAndUsers,groupAndUser)
                .join(groupAndUser.user, user)
                .join(user.memories,memory)
                // 3. 이제 그 유저가 어떤 그룹에 속해있는지를 체크할 예정
                .where(
                        // 1. 그 그룹에 속한 사람이여야 함
                        userGroup.id.eq(groupId),
                        // 2. 메모리의 그룹도 이 그룹이여야 함.
                        memory.groupInfo.groupId.eq(groupId),
                        memory.openType.ne(OpenType.PRIVATE),
                        memoryBlocked(userBlockedMemoryId).not(),
                        eqUser(userSeq)

                )
                .distinct()
                .offset(pageable.getOffset())
                .orderBy(ORDERS.stream().toArray(OrderSpecifier[]::new))
                .limit(pageable.getPageSize() + 1)
                .fetch();



        return checkLastPage(pageable, results);



    }



    private BooleanExpression eqUser(Long userSeq) {
        if(userSeq == null || userSeq == -1)
        {
            return null;
        }
        return memory.user.userSeq.eq(userSeq);
    }

    private List<OrderSpecifier> getAllOrderSpecifiers(Pageable pageable) {

        List<OrderSpecifier> ORDERS = new ArrayList<>();

        if (!isEmpty(pageable.getSort())) {
            for (Sort.Order order : pageable.getSort()) {
                Order direction = order.getDirection().isAscending() ? Order.ASC : Order.DESC;

                switch (order.getProperty()) {
                    case "visitedDate":
                        OrderSpecifier<?> visitedDate = QueryDslUtil
                                .getSortedColumn(direction, memory, "visitedDate");
                        ORDERS.add(visitedDate);
                        break;
                    case "stars":
                        OrderSpecifier<?> stars = QueryDslUtil
                                .getSortedColumn(direction, memory, "stars");
                        ORDERS.add(stars);
                        break;
                    default:
                        break;
                }
            }
        }

        return ORDERS;
    }


    private BooleanExpression memoryBlocked(List<Long> compare) {

        if(compare == null)
        {
            return null;
        }

        return memory.id.in(compare);
    }


    private Slice<Memory> checkLastPage(Pageable pageable, List<Memory> results) {

        boolean hasNext = false;

        // 조회한 결과 개수가 요청한 페이지 사이즈보다 크면 뒤에 더 있음, next = true
        if (results.size() > pageable.getPageSize()) {
            hasNext = true;
            results.remove(pageable.getPageSize());
        }

        return new SliceImpl<>(results, pageable, hasNext);
    }

    private BooleanExpression eqGroup(GroupType groupType)
    {
        if(groupType == null || groupType == GroupType.ALL)
        {
            return null;
        }
        return memory.groupInfo.groupType.eq(groupType);
    }




}