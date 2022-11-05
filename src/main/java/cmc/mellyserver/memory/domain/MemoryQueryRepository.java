package cmc.mellyserver.memory.domain;

import cmc.mellyserver.common.util.jpa.QueryDslUtil;
import cmc.mellyserver.group.domain.enums.GroupType;
import cmc.mellyserver.memory.domain.enums.OpenType;
import cmc.mellyserver.memory.presentation.dto.request.MemorySearchDto;
import cmc.mellyserver.place.domain.QPlace;
import cmc.mellyserver.user.domain.User;
import com.querydsl.core.types.Order;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.Projections;
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

import static cmc.mellyserver.group.domain.QGroupAndUser.*;
import static cmc.mellyserver.group.domain.QUserGroup.*;
import static cmc.mellyserver.memory.domain.QMemory.*;
import static cmc.mellyserver.place.domain.QPlace.*;
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



    /**
     * 포함하고 있는 메모리 제목으로 장소 조회 (최적화 완료 -> 필요할때 fetch join 도입)
     * query projection과 fetch join은 동시 사용 불가 -> 둘 중 하나만 선택하자!
     */
    public List<MemorySearchDto> searchMemoryName(Long userSeq, String memoryName) {
        return query.select(Projections.constructor(MemorySearchDto.class, memory.place.id, memory.title))
                .from(memory)
                .where(memory.user.userSeq.eq(userSeq),
                        memory.isReported.eq(false),
                        memory.title.contains(memoryName))
                .distinct().fetch();
    }



    /**
     * 장소 상세 - 나의 메모리 (최적화 완료, 인덱스 추가 필요)
     */
    public Slice<Memory> searchMemoryUserCreate(Pageable pageable, String uid, Long placeId,GroupType groupType) {

        List<OrderSpecifier> ORDERS = getAllOrderSpecifiers(pageable);

        List<Memory> results = query.select(memory)
                .from(memory)
                .join(memory.place,place).fetchJoin()
                .join(memory.user,user).fetchJoin()
                .where(
                        eqPlace(placeId),  // 특정 장소에 대한 메모리면 placeId로 필터링
                        eqUserId(uid),  // 내가 작성한 메모리가 맞는지 체크
                        eqGroup(groupType)   // 그룹 타입 체크
                ).orderBy(ORDERS.stream().toArray(OrderSpecifier[]::new))  // Sort에 명시한 조건으로 필터링
                .offset(pageable.getOffset())   // offset 지정
                .limit(pageable.getPageSize() + 1)  // 하나 더 땡겨와서 마지막 페이지인지 체크
                .fetch();

        return checkLastPage(pageable, results); // 마지막 페이지인지 체크 후 SliceImpl 반환해주기
    }



    /**
     * 장소 상세 - 다른 사람이 작성한 메모리 (최적화 완료,인덱스 추가 필요)
     */
    public Slice<Memory> searchMemoryOtherCreate(Pageable pageable, String uid, Long placeId,GroupType groupType) {

        List<OrderSpecifier> ORDERS = getAllOrderSpecifiers(pageable);

        List<Memory> results = query.select(memory)
                .from(memory)
                .join(memory.place,place).fetchJoin()
                .join(memory.user,user).fetchJoin()
                .where(
                        eqPlace(placeId),
                        neUserId(uid),  // 로그인한 유저가 작성한 메모리가 아닐때
                        eqGroup(groupType),
                        memory.isReported.eq(false),
                        memory.openType.eq(OpenType.ALL) // 전체 공개로 올린 메모리만 보여주기
                ).orderBy(ORDERS.stream().toArray(OrderSpecifier[]::new))
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize() + 1)
                .fetch();

        return checkLastPage(pageable, results);
    }



    /**
     * 마이페이지 - 내가 스크랩한 메모리 (차후에 추가 및 최적화 예정)
     */
    public Slice<Memory> getScrapedMemory(Pageable pageable, User user,GroupType groupType) {

        List<OrderSpecifier> ORDERS = getAllOrderSpecifiers(pageable);

        List<Memory> results = query.select(memory)
                .from(memory)
                .where(
                        eqGroup(groupType),
                        memory.isReported.eq(false),
                        memory.id.in(user.getMemoryScraps().stream().map(s -> s.getMemory().getId()).collect(Collectors.toList()))
                )
                .orderBy(ORDERS.stream().toArray(OrderSpecifier[]::new))
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize() + 1)
                .fetch();

        return checkLastPage(pageable, results);
    }



    /**
     * 마이페이지 - 내 그룹만 필터 조회 (최적화 완료,인덱스 필요)
     */
    public Slice<Memory> getMyGroupMemory(Pageable pageable, String uid, Long placeId,GroupType groupType) {

        List<OrderSpecifier> ORDERS = getAllOrderSpecifiers(pageable);

        List<Memory> results = query.select(memory)
                .from(memory)
                .join(memory.user,user).fetchJoin()
                .join(memory.place,place).fetchJoin()
                .join(user.groupAndUsers, groupAndUser) // 컬렉션 조회이므로 fetch join 걸지 않고 in 쿼리로 가져오기
                .where(
                        eqPlace(placeId),
                        eqGroup(groupType),
                        memory.isReported.eq(false),
                        memory.openType.ne(OpenType.PRIVATE),
                        // 2. 내가 속해있는 그룹에 속해있는 사람들 -> 이건 작성자가 속해있는 그룹 체크
                        groupAndUser.group.id.in(
                                JPAExpressions.select(userGroup.id)
                                        .from(groupAndUser)
                                        .join(groupAndUser.group, userGroup)
                                        .join(groupAndUser.user, user)
                                        .where(user.userId.eq(uid))
                        ),
                        checkGroup(uid).not(),
                        neUserId(uid)
                )
                .distinct()
                .offset(pageable.getOffset())
                .orderBy(ORDERS.stream().toArray(OrderSpecifier[]::new))
                .limit(pageable.getPageSize() + 1)
                .fetch();


        return checkLastPage(pageable, results);
    }



    private BooleanExpression checkGroup(String uid) {

        if(uid == null)
        {
            return null;
        }

        // 내가 속해있는 그룹이 아니면서, 메모리 타입이 그룹인 것들 -> 어떤 그룹을 대상으로 썼는지 체크
        return memory.groupInfo.groupId.notIn(
                JPAExpressions.select(userGroup.id)
                        .from(groupAndUser)
                        .join(groupAndUser.group, userGroup)
                        .join(groupAndUser.user, user)
                        .where(user.userId.eq(uid))
        ).and(memory.openType.eq(OpenType.GROUP));
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



    private BooleanExpression eqUserId(String uid)
    {
        if(uid == null)
        {
            return null;
        }
        return memory.user.userId.eq(uid);
    }



    private BooleanExpression neUserId(String uid)
    {
        if(uid == null)
        {
            return null;
        }
        return memory.user.userId.ne(uid);
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
