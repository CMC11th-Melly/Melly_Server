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
     * @param userSeq      유저 식별값
     * @param placeId      장소 아이디
     * @param keyword      메모리 작성시 설정한 키워드
     * @param groupType    메모리가 속한 그룹의 타입
     * @param visitiedDate 메모리의 장소에 방문한 날짜
     * 만약 groupType이 null이나 ALL로 들어가면 조건 아예 없어버림. size만 처음에 10 넣고, 다음부터 마지막 데이터 가져오기
     */
    public Slice<Memory> searchMemoryUserCreate(Long lastMemoryId, Pageable pageable, Long userSeq, Long placeId, String keyword, GroupType groupType, String visitiedDate) {

        List<Memory> results = query.select(memory)
                .from(memory)
                .where(
                        ltMemoryId(lastMemoryId),  // 메모리 id를 커서로 사용해서 페이징 -> 인덱스 사용
                        eqPlace(placeId),          // 특정 장소에 대한 메모리를 가져옴
                        eqUserSeq(userSeq),  // 특정 장소에 대한 메모리들 중 유저가 작성한 메모리 가져옴
                        eqKeyword(keyword),      // 해당 키워드가 필요한 메모리
                        eqGroup(groupType),      // 특정 groupType에 포함되는 메모리
                        eqVisitiedDate(visitiedDate)  // 특정 날짜에 속하는 메모리
                ).orderBy(memory.visitedDate.desc())          // memoryId 순서로 내림차순
                .limit(pageable.getPageSize() + 1)
                .fetch();
        return checkLastPage(pageable, results);
    }

    public Slice<Memory> searchMemoryOtherCreate(Long lastId, Pageable pageable, Long userSeq, Long placeId,GroupType groupType, String keyword, String visitiedDate) {

        List<Memory> results = query.select(memory)
                .from(memory)

                .where(
                        // 커서 기반 페이징 용
                        ltMemoryId(lastId),
                        // 특정 장소의 메모리 가져오기
                        eqPlace(placeId),
                        // 내가 작성하지 않은 메모리만 가져오기
                        neUserSeq(userSeq),
                        // 내가 설정한 groupType으로 카테고리 필터링
                        eqGroup(groupType),

                        // 상대방 메모리 중에 전체 공개인것만 가져오기
                        memory.openType.eq(OpenType.ALL),
                        // 키워드만 필터링
                        eqKeyword(keyword),
                        // 날짜로 필터링
                        eqVisitiedDate(visitiedDate)
                ).orderBy(memory.id.desc())
                .limit(pageable.getPageSize() + 1)
                .fetch();

        return checkLastPage(pageable, results);
    }

    public Slice<Memory> searchMemoryMyGroupCreate(Long lastId, Pageable pageable, Long userSeq,Long groupId, Long placeId,GroupType groupType, String keyword, String visitiedDate) {

        List<Memory> results = query.select(memory)
                .from(memory)
                .where(
                        // 커서 기반 페이징 용
                        ltMemoryId(lastId),
                        // 특정 장소의 메모리 가져오기
                        eqPlace(placeId),
                        // 내가 작성하지 않은 메모리만 가져오기
                        neUserSeq(userSeq),
                        inSameGroup(groupId),
                        // 내가 설정한 groupType으로 카테고리 필터링
                        eqGroup(groupType),
                        // 상대방 메모리 중에 전체 공개인것만 가져오기
                        memory.openType.eq(OpenType.ALL),
                        // 키워드만 필터링
                        eqKeyword(keyword),
                        // 날짜로 필터링
                        eqVisitiedDate(visitiedDate)
                ).orderBy(memory.id.desc())
                .limit(pageable.getPageSize() + 1)
                .fetch();

        return checkLastPage(pageable, results);
    }

    public Slice<Memory> getScrapedMemory(Long lastId, Pageable pageable, User user,GroupType groupType) {
        List<Memory> results = query.select(memory)
                .from(memory)
                .where(
                        ltMemoryId(lastId),
                        eqGroup(groupType),
                        memory.id.in(user.getMemoryScraps().stream().map(s -> s.getMemory().getId()).collect(Collectors.toList()))
                ).orderBy(memory.id.desc())
                .limit(pageable.getPageSize() + 1)
                .fetch();
        return checkLastPage(pageable, results);
    }

    /**
     * 1. 내가 속해있는 모든 그룹들을 찾아야해
     */
    public Slice<Memory> getMyGroupMemory(Long lastId, Pageable pageable, User loginUser, Long placeId) {



        // 1. 메모리를 가져올꺼야
        List<Memory> results = query.selectFrom(memory)
                // 2. 메모리와 그 메모리를 가진 유저를 조인
                .join(memory.user, user)
                .join(user.groupAndUsers, groupAndUser)
                // 3. 이제 그 유저가 어떤 그룹에 속해있는지를 체크할 예정
                .where(
                        ltMemoryId(lastId),
                        // 1. 일단 특정 장소에 속해야 하고
                        eqPlace(placeId),
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
                .orderBy(memory.id.desc())
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


    private BooleanExpression eqKeyword(String keyword) {

        if (keyword == null || keyword.isEmpty()) {
            return null;
        }

        return memory.keyword.contains(keyword);
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


    private BooleanExpression eqVisitiedDate(String visitiedDate) {

        if (visitiedDate == null || visitiedDate.isEmpty()) {
            return null;
        }

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMdd");
        LocalDate visited = LocalDate.parse(visitiedDate, formatter);

        return memory.visitedDate.between(
                visited.atStartOfDay(),
                LocalDateTime.of(visited, LocalTime.of(23, 59, 59)));


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


    private BooleanExpression ltMemoryId(Long memoryId) {

        if (memoryId == null || memoryId == -1) {
            return null;
        }

        return memory.id.lt(memoryId);
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
                    default:
                        break;
                }
            }
        }

        return ORDERS;
    }



}
