package cmc.mellyserver.memory.domain;

import cmc.mellyserver.common.util.jpa.QueryDslUtil;
import cmc.mellyserver.group.domain.GroupAndUser;
import cmc.mellyserver.group.domain.QGroupAndUser;
import cmc.mellyserver.group.domain.QUserGroup;
import cmc.mellyserver.group.domain.UserGroup;
import cmc.mellyserver.group.domain.enums.GroupType;
import cmc.mellyserver.memory.application.dto.MemoryForGroupResponse;
import cmc.mellyserver.memory.domain.enums.OpenType;
import cmc.mellyserver.memory.presentation.dto.ImageDto;
import cmc.mellyserver.memory.presentation.dto.MemorySearchDto;
import cmc.mellyserver.place.domain.Place;
import cmc.mellyserver.user.domain.QUser;
import cmc.mellyserver.user.domain.User;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.Order;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.Predicate;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.JPAExpressions;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.geolatte.geom.M;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.SliceImpl;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static cmc.mellyserver.group.domain.QGroupAndUser.*;
import static cmc.mellyserver.group.domain.QUserGroup.*;
import static cmc.mellyserver.memory.domain.QMemory.*;
import static cmc.mellyserver.place.domain.QPlace.place;
import static cmc.mellyserver.user.domain.QUser.*;
import static org.springframework.util.ObjectUtils.isEmpty;

@Repository
public class MemoryQueryRepository {


    private final EntityManager em;
    private final JPAQueryFactory query;

    public MemoryQueryRepository(EntityManager em) {
        this.em = em;
        this.query = new JPAQueryFactory(em);
    }


    public List<MemorySearchDto> searchMemoryName(Long userSeq, String memoryName) {
        return query.select(Projections.constructor(MemorySearchDto.class, memory.place.id, memory.title))
                .from(memory)
                .where(memory.user.userSeq.eq(userSeq), memory.title.contains(memoryName)).distinct().fetch();
    }


    /**
     * 장소 상세 - 나의 메모리
     */
    public Slice<Memory> searchMemoryUserCreate(Pageable pageable, Long userSeq, Long placeId,GroupType groupType) {

        List<OrderSpecifier> ORDERS = getAllOrderSpecifiers(pageable);

        List<Memory> results = query.select(memory)
                .from(memory)
                .where(
                        eqPlace(placeId),  // 특정 장소에 대한 메모리면 placeId로 필터링
                        eqUserSeq(userSeq),  // 내가 작성한 메모리가 맞는지 체크
                        eqGroup(groupType)   // 그룹 타입 체크
                ).orderBy(ORDERS.stream().toArray(OrderSpecifier[]::new))  // Sort에 명시한 조건으로 필터링
                .offset(pageable.getOffset())   // offset 지정
                .limit(pageable.getPageSize() + 1)  // 하나 더 땡겨와서 마지막 페이지인지 체크
                .fetch();
        return checkLastPage(pageable, results);
    }

    /**
     * 장소 상세 - 이 장소 메모리
     */
    public Slice<Memory> searchMemoryOtherCreate(Pageable pageable, Long userSeq, Long placeId,GroupType groupType) {

        List<OrderSpecifier> ORDERS = getAllOrderSpecifiers(pageable);

        List<Memory> results = query.select(memory)
                .from(memory)

                .where(
                        eqPlace(placeId),
                        neUserSeq(userSeq),
                        eqGroup(groupType),
                        memory.openType.eq(OpenType.ALL)
                ).orderBy(ORDERS.stream().toArray(OrderSpecifier[]::new))
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize() + 1)
                .fetch();

        return checkLastPage(pageable, results);
    }


    /**
     * 마이페이지 - 내가 스크랩한 장소
     */
    public Slice<Memory> getScrapedMemory(Pageable pageable, User user,GroupType groupType) {

        List<OrderSpecifier> ORDERS = getAllOrderSpecifiers(pageable);

        List<Memory> results = query.select(memory)
                .from(memory)
                .where(
                        eqGroup(groupType),
                        memory.id.in(user.getMemoryScraps().stream().map(s -> s.getMemory().getId()).collect(Collectors.toList()))
                ).orderBy(ORDERS.stream().toArray(OrderSpecifier[]::new))
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize() + 1)
                .fetch();
        return checkLastPage(pageable, results);
    }


    /**
     * 마이페이지 - 내가 속해있는 모든 그룹의 이 장소에 대한 메모리
     */
    public Slice<Memory> getMyGroupMemory(Pageable pageable, User loginUser, Long placeId,GroupType groupType) {

        List<OrderSpecifier> ORDERS = getAllOrderSpecifiers(pageable);
        // 1. 메모리를 가져올꺼야
        List<Memory> results = query.selectFrom(memory)
                // 2. 메모리와 그 메모리를 가진 유저를 조인
                .join(memory.user, user)
                .join(user.groupAndUsers, groupAndUser)
                // 3. 이제 그 유저가 어떤 그룹에 속해있는지를 체크할 예정
                .where(
                        // 1. 일단 특정 장소에 속해야 하고
                        eqPlace(placeId),
                        eqGroup(groupType),
                        // 2. 내가 속해있는 그룹에 속해있는 사람들
                        groupAndUser.group.id.in(
                                JPAExpressions.select(userGroup.id)
                                        .from(groupAndUser)
                                        .join(groupAndUser.group, userGroup)
                                        .join(groupAndUser.user, user)
                                        .where(user.userSeq.eq(loginUser.getUserSeq()))
                        ),
                        memory.openType.ne(OpenType.PRIVATE),
                        checkGroup(loginUser).not(),
                        // 3. 하지만 나 자신은 제외
                        user.userSeq.ne(loginUser.getUserSeq())
                )
                .distinct()
                .offset(pageable.getOffset())
                .orderBy(ORDERS.stream().toArray(OrderSpecifier[]::new))
                .limit(pageable.getPageSize() + 1)
                .fetch();


        return checkLastPage(pageable, results);
    }


    private BooleanExpression checkGroup(User loginUser) {

        if(loginUser == null)
        {
            return null;
        }

        return memory.groupInfo.groupId.notIn(
                JPAExpressions.select(userGroup.id)
                        .from(groupAndUser)
                        .join(groupAndUser.group, userGroup)
                        .join(groupAndUser.user, user)
                        .where(user.userSeq.eq(loginUser.getUserSeq()))
        ).and(memory.openType.eq(OpenType.GROUP));
    }



    private BooleanExpression inSameGroup(Long groupId)
    {
        if(groupId == null)
        {
            return null;
        }
        return memory.groupInfo.groupId.eq(groupId);
    }


    private BooleanExpression eqGroup(GroupType groupType) {

        if (groupType == null || groupType == GroupType.ALL) {
            return null;
        }


        return memory.groupInfo.groupType.eq(groupType);
    }










    private BooleanExpression eqPlace(Long placeId) {
        if (placeId == null) {
            return null;
        }
        return memory.place.id.eq(placeId);
    }

    private BooleanExpression eqUserSeq(Long userSeq)
    {
        if(userSeq == null)
        {
            return null;
        }
        return memory.user.userSeq.eq(userSeq);
    }


    private BooleanExpression neUserSeq(Long userSeq)
    {
        if(userSeq == null)
        {
            return null;
        }
        return memory.user.userSeq.ne(userSeq);
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



}
