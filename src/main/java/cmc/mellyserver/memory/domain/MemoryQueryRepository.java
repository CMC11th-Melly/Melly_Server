package cmc.mellyserver.memory.domain;

import cmc.mellyserver.common.enums.OpenType;
import cmc.mellyserver.common.util.jpa.QueryDslUtil;
import cmc.mellyserver.common.enums.GroupType;
import cmc.mellyserver.memory.domain.dto.KeywordResponse;
import cmc.mellyserver.memory.domain.dto.UserCreatedMemoryListResponseDto;
import cmc.mellyserver.memory.presentation.dto.common.ImageDto;
import cmc.mellyserver.memory.presentation.dto.request.MemorySearchDto;
import cmc.mellyserver.user.domain.User;
import com.querydsl.core.types.Order;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Repository;
import javax.persistence.EntityManager;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import static cmc.mellyserver.memory.domain.QMemory.*;
import static cmc.mellyserver.memory.domain.QMemoryImage.memoryImage;
import static cmc.mellyserver.place.domain.QPlace.place;
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
     * TODO : 찐막 수정 완료
     */
    public List<MemorySearchDto> searchMemoryName(Long userSeq, String memoryName) {
        return query.select(Projections.constructor(MemorySearchDto.class, memory.placeId, memory.title))
                .from(memory)
                .where(
                        memory.userId.eq(userSeq),  // 본인이 가지고 있는 메모리
                        memory.isReported.isFalse(),      // 신고되지 않은 메모리
                        memory.isDelete.isFalse(),        // 삭제 되지 않은 메모리
                        memory.title.contains(memoryName)) // 메모리 제목으로 검색하는 로직
                .distinct().fetch();
    }



    /**
     * 장소 상세 - 나의 메모리, 마이페이지 - 내가 작성한 메모리 조회 (최적화 완료, 인덱스 추가 필요)
     * TODO : 찐막 검증 완료
     */
    public Slice<UserCreatedMemoryListResponseDto> searchMemoryUserCreatedForPlace(Pageable pageable, Long userSeq,Long placeId, GroupType groupType) {

        List<OrderSpecifier> ORDERS = getAllOrderSpecifiers(pageable);

        List<UserCreatedMemoryListResponseDto> results = query.select(Projections.constructor(UserCreatedMemoryListResponseDto.class, place.id, place.placeName, memory.id, memory.title, memory.content, memory.groupInfo.groupType, memory.groupInfo.groupName, memory.stars, memory.isDelete, memory.visitedDate))
                .from(memory)
                .leftJoin(place).on(place.id.eq(memory.placeId))
                .where(
                        memory.userId.eq(userSeq), // 내가 작성자인 메모리
                        eqGroup(groupType),// 그룹 타입 필터링 용
                        memory.placeId.eq(placeId)
                )
                .orderBy(
                        ORDERS.stream().toArray(OrderSpecifier[]::new)
                )
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize() + 1)
                .fetch();

        Map<Long, List<ImageDto>> memoryImageList = findMemoryImage(toMemoryIds(results));
        Map<Long, List<KeywordResponse>> keywordList = findKeywordList(toMemoryIds(results));

        results.forEach(f -> {
            f.setMemoryImages(memoryImageList.get(f.getMemoryId()));
            f.setKeyword(keywordList.get(f.getMemoryId()).stream().map(KeywordResponse::getKeyword).collect(Collectors.toList()));
        });

        boolean hasNext = false;

        // 조회한 결과 개수가 요청한 페이지 사이즈보다 크면 뒤에 더 있음, next = true
        if (results.size() > pageable.getPageSize()) {
            hasNext = true;
            results.remove(pageable.getPageSize());
        }

        return new SliceImpl<>(results, pageable,hasNext);
    }

    // TODO : 다시 안봐
    public Page<UserCreatedMemoryListResponseDto> searchMemoryUserCreatedForMyPage(Pageable pageable, Long userSeq, GroupType groupType) {

        List<OrderSpecifier> ORDERS = getAllOrderSpecifiers(pageable);

        List<UserCreatedMemoryListResponseDto> fetch = query.select(Projections.constructor(UserCreatedMemoryListResponseDto.class, place.id, place.placeName, memory.id, memory.title, memory.content, memory.groupInfo.groupType, memory.groupInfo.groupName, memory.stars, memory.isDelete, memory.visitedDate))
                .from(memory)
                .leftJoin(place).on(place.id.eq(memory.placeId))
                .where(
                        memory.userId.eq(userSeq), // 내가 작성자인 메모리
                        eqGroup(groupType)// 그룹 타입 필터링 용
                )
                .orderBy(
                        ORDERS.stream().toArray(OrderSpecifier[]::new)
                )
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize() + 1)
                .fetch();

        Map<Long, List<ImageDto>> memoryImageList = findMemoryImage(toMemoryIds(fetch));
        Map<Long, List<KeywordResponse>> keywordList = findKeywordList(toMemoryIds(fetch));

        fetch.forEach(f -> {
            f.setMemoryImages(memoryImageList.get(f.getMemoryId()));
            f.setKeyword(keywordList.get(f.getMemoryId()).stream().map(KeywordResponse::getKeyword).collect(Collectors.toList()));
        });

        return new PageImpl<>(fetch);
    }




    /**
     * TODO : 장소 상세 - 다른 사람이 작성한 메모리 (최적화 완료,인덱스 추가 필요), 수정 완료
     * TODO : 찐막 수정 완료
     */
    public Slice<UserCreatedMemoryListResponseDto> searchMemoryOtherCreate(Pageable pageable, Long userSeq, Long placeId,GroupType groupType) {

        List<OrderSpecifier> ORDERS = getAllOrderSpecifiers(pageable);

        List<UserCreatedMemoryListResponseDto> results = query.select(Projections.constructor(UserCreatedMemoryListResponseDto.class, place.id, place.placeName, memory.id, memory.title, memory.content, memory.groupInfo.groupType, memory.groupInfo.groupName, memory.stars, memory.isDelete, memory.visitedDate)) //  다른 사람이 작성한 메모리 가져오기
                .from(memory)
                .leftJoin(place).on(place.id.eq(memory.placeId))
                .where(
                        memory.placeId.eq(placeId),  // 특정 장소에 속해있는지 체크
                        memory.userId.ne(userSeq), // 로그인 유저가 아닌 다른 사람이 작성했는지 체크
                        eqGroup(groupType), // 그룹 타입 체크
                        memory.isReported.isFalse(), // 신고되지 않은 메모리 조회
                        memory.isDelete.isFalse(),   // 삭제되지 않은 메모리 조회
//                        checkMemoryNotBlocked(userBlockedMemoryId), // 다른 사람이 작성했더라도, 내가 차단한 메모리는 안보이게 하기
                        memory.openType.eq(OpenType.ALL) // 다른 사람이 전체 공개로 올린 메모리만 보여주기
                )
                .orderBy(ORDERS.stream().toArray(OrderSpecifier[]::new))
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize() + 1)
                .fetch();


        boolean hasNext = false;

        // 조회한 결과 개수가 요청한 페이지 사이즈보다 크면 뒤에 더 있음, next = true
        if (results.size() > pageable.getPageSize()) {
            hasNext = true;
            results.remove(pageable.getPageSize());
        }

        return new SliceImpl<>(results, pageable,hasNext);
    }





    /**
     * 마이페이지 - 내 그룹만 필터 조회 (최적화 완료,인덱스 필요) 내 그룹 사람들이 쓴 메모리 조회
     */
    public Slice<Memory> getMyGroupMemory(Pageable pageable, User loginUser, Long placeId,GroupType groupType) {

        List<OrderSpecifier> ORDERS = getAllOrderSpecifiers(pageable);
//        List<Long> userBlockedMemoryId = getBlockedMemoryId(loginUser);
//
//        query.select(memory)
//                .from(memory)
//                .leftJoin(user).on(memory.userId.eq(loginUser.getUserSeq())) // 유저 정보 조인

//        List<Long> groupIds = loginUser.getGroupIds(); // 현재 로그인한 유저가 속해있는 그룹의 ID들 체크


//        List<Memory> results = query.select(memory)
//                .from(memory)
//                .leftJoin(user).on(user.userSeq.eq(memory.userId))
////                .join(memory.place,place).fetchJoin()
////                .join(user.groupAndUsers, groupAndUser) // 컬렉션 조회이므로 fetch join 걸지 않고 in 쿼리로 가져오기
//                .join(userGroup).on(userGroup.id.in(user.groupIds))
//                .where(
//                        memory.placeId.eq(placeId),  // 특정 장소에 속해있는 메모리인지 체크
//                        eqGroup(groupType),  // 내가 원하는 그룹에 속해있는 데이터만 가져오기
//                        memory.isReported.isFalse(), // 신고되지 않은 메모리만 가져오기
//                        memory.isDelete.isFalse(), // 삭제되지 않은 메모리만 가져오기
//                        memory.openType.ne(OpenType.PRIVATE), // 비공개가 아닌 것만 다 가져오기
//                        user.group
//                        // 2. 내가 속해있는 그룹에 속해있는 사람들 -> 이건 작성자가 속해있는 그룹 체크
//                        groupAndUser.group.id.in(
//                                JPAExpressions.select(userGroup.id)
//                                        .from(groupAndUser)
//                                        .join(groupAndUser.group, userGroup)
//                                        .join(groupAndUser.user, user)
//                                        .where(user.userId.eq(loginUser.getUserId()))
//                        ),
//                        checkMemoryNotBlocked(userBlockedMemoryId),
//                        checkGroup(loginUser.getUserId()).not(),
//                        memory.userId.ne(loginUser.getUserSeq())
//                )
//                .distinct()
//                .offset(pageable.getOffset())
//                .orderBy(ORDERS.stream().toArray(OrderSpecifier[]::new))
//                .limit(pageable.getPageSize() + 1)
//                .fetch();
//
//
//        return checkLastPage(pageable, results);
        return null;
    }


    // TODO : Block한 메모리 ID만 가지고 있어야지...
//    private List<Long> getBlockedMemoryId(User user)
//    {
////        return user.getMemoryBlocks().stream().map(m -> m.getMemory().getId()).collect(Collectors.toList());
//       return user.getBlockedMemoryIds();
//    }

//    private BooleanExpression checkGroup(String uid) {
//
//        if(uid == null)
//        {
//            return null;
//        }
//
//        // 내가 속해있는 그룹이 아니면서, 메모리 타입이 그룹인 것들 -> 어떤 그룹을 대상으로 썼는지 체크
//        return memory.groupInfo.groupId.notIn(
//                JPAExpressions.select(userGroup.id)
//                        .from(groupAndUser)
//                        .join(groupAndUser.group, userGroup)
//                        .join(groupAndUser.user, user)
//                        .where(user.userId.eq(uid))
//        ).and(memory.openType.eq(OpenType.GROUP));
//    }

    private BooleanExpression checkMemoryNotBlocked(List<Long> compare) {

        if(compare == null)
        {
            return null;
        }

        return memory.id.notIn(compare);
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
        return memory.placeId.eq(placeId);
    }



    private BooleanExpression eqUserId(Long userSeq)
    {
        if(userSeq == null)
        {
            return null;
        }
        return memory.userId.eq(userSeq);
    }



    private BooleanExpression neUser(User user)
    {
        if(user == null)
        {
            return null;
        }
        return memory.userId.ne(user.getUserSeq());
    }

    private BooleanExpression neUserId(Long userSeq)
    {
        if(userSeq == null)
        {
            return null;
        }
        return memory.userId.ne(userSeq);
    }



    private Slice<?> checkLastPage(Pageable pageable, List<?> results) {

        boolean hasNext = false;

        // 조회한 결과 개수가 요청한 페이지 사이즈보다 크면 뒤에 더 있음, next = true
        if (results.size() > pageable.getPageSize()) {
            hasNext = true;
            results.remove(pageable.getPageSize());
        }

        return new SliceImpl<>(results, pageable,hasNext);
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

    public HashMap<String,Long> countMemoryOfPlace(Long placeId, Long userSeq) {

        Long myMemoryCount = query.select(memory.count())
                .from(memory)
                .where(
                        memory.placeId.eq(placeId),
                        memory.userId.eq(userSeq)
                )
                .fetchOne();

        Long otherMemoryCount = query.select(memory.count())
                .from(memory)
                .where(
                        memory.placeId.eq(placeId), // 해당 장소에 속해 있지만
                        memory.userId.ne(userSeq)  // 해당 유저의 아이디를 가지고 있지 않고
                        // TODO : 메모리 블록되지 않았다는거 보장해야 한다 로직 추가 예정 2023.3.30
                )
                .fetchOne();

        HashMap<String,Long> map = new HashMap<>();
        map.put("myMemoryCount",myMemoryCount);
        map.put("otherMemoryCount",otherMemoryCount);

        return map;

    }

    public HashMap<String,Long> countMemoriesBelongToPlace(Long userSeq, Long placeId) {

        Long memoriesBelongToLoginUser = query.select(memory.count())
                .from(memory)
                .where(
                        memory.placeId.eq(placeId),
                        memory.userId.eq(userSeq)
                ).fetchOne();

        Long memoriesNotBelongToLoginUser = query.select(memory.count())
                .from(memory)
                .where(
                        memory.placeId.eq(placeId),
                        memory.userId.ne(userSeq)
                ).fetchOne();

        HashMap<String, Long> map = new HashMap<>();
        map.put("belongToUser",memoriesBelongToLoginUser);
        map.put("notBelongToUser",memoriesNotBelongToLoginUser);

        return map;
    }

    private List<Long> toMemoryIds(List<UserCreatedMemoryListResponseDto> result) {
        return result.stream()
                .map(o -> o.getMemoryId())
                .collect(Collectors.toList());
    }

    private Map<Long, List<KeywordResponse>> findKeywordList(List<Long> memoryIds) {

        List<KeywordResponse> result = em.createNativeQuery("select new cmc.mellyserver.memory.domain.dto.KeywordResponse(kt.memory_id,kt.keyword)  from keywords_table kt where keywords_table.memory_id in :memoryIds", KeywordResponse.class)
                .setParameter("memoryIds", memoryIds)
                .getResultList();

        return result.stream()
                .collect(Collectors.groupingBy(KeywordResponse::getMemoryId));
    }


    private Map<Long, List<ImageDto>> findMemoryImage(List<Long> memoryIds) {

        List<ImageDto> result = query.select(Projections.constructor(ImageDto.class, memoryImage.memory.id, memoryImage.id, memoryImage.imagePath))
                .from(memoryImage)
                .where(memoryImage.id.in(memoryIds))
                .fetch();

        return result.stream()
                .collect(Collectors.groupingBy(ImageDto::getMemoryId));
    }
}


