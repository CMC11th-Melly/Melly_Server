package cmc.mellyserver.mellycore.memory.domain.repository;


import cmc.mellyserver.mellycommon.enums.GroupType;
import cmc.mellyserver.mellycommon.enums.OpenType;
import cmc.mellyserver.mellycore.common.util.jpa.QueryDslUtil;
import cmc.mellyserver.mellycore.memory.domain.repository.dto.ImageDto;
import cmc.mellyserver.mellycore.memory.domain.repository.dto.KeywordResponseDto;
import cmc.mellyserver.mellycore.memory.domain.repository.dto.MemoryResponseDto;
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
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static cmc.mellyserver.mellycore.group.domain.QGroupAndUser.groupAndUser;
import static cmc.mellyserver.mellycore.memory.domain.QMemory.memory;
import static cmc.mellyserver.mellycore.memory.domain.QMemoryImage.memoryImage;
import static cmc.mellyserver.mellycore.place.domain.QPlace.place;
import static org.springframework.util.ObjectUtils.isEmpty;


@Repository
public class MemoryQueryRepository {

    private final EntityManager em;
    private JPAQueryFactory query;

    public MemoryQueryRepository(EntityManager em) {
        this.em = em;
        query = new JPAQueryFactory(em);
    }


    public Slice<MemoryResponseDto> searchMemoryUserCreatedForPlace(Pageable pageable, Long userSeq, Long placeId, GroupType groupType) {

        List<OrderSpecifier> ORDERS = getAllOrderSpecifiers(pageable);

        List<MemoryResponseDto> results = query.select(Projections.constructor(MemoryResponseDto.class,
                        place.id,
                        place.placeName,
                        memory.id,
                        memory.title,
                        memory.content,
                        memory.stars,
                        createdByLoginUser(userSeq),
                        memory.visitedDate))
                .from(memory)
                .innerJoin(place).on(place.id.eq(memory.placeId)).fetchJoin()
                .where(
                        createdByLoginUser(userSeq),
                        eqPlace(placeId),
                        eqGroup(groupType)
                )
                .orderBy(ORDERS.stream().toArray(OrderSpecifier[]::new))
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize() + 1)
                .fetch();

        initMemoryImageAndKeyword(results);
        return transferToSlice(pageable, results);
    }

    public Slice<MemoryResponseDto> getMyGroupMemory(Pageable pageable, Long groupId, Long userSeq) {

        List<OrderSpecifier> ORDERS = getAllOrderSpecifiers(pageable);

        List<MemoryResponseDto> results = query.select(Projections.constructor(MemoryResponseDto.class,
                        place.id,
                        place.placeName,
                        memory.id,
                        memory.title,
                        memory.content,

                        memory.stars,
                        createdByLoginUser(userSeq),
                        memory.visitedDate))
                .from(memory)
                .innerJoin(place).on(place.id.eq(memory.placeId))
                .where(
                        memory.groupId.eq(groupId),
                        checkOpenTypeAllOrGroup()
                )
                .offset(pageable.getOffset())
                .orderBy(ORDERS.stream().toArray(OrderSpecifier[]::new))
                .limit(pageable.getPageSize() + 1)
                .fetch();

        initMemoryImageAndKeyword(results);
        return transferToSlice(pageable, results);
    }

    private BooleanExpression checkOpenTypeAllOrGroup() {
        return memory.openType.ne(OpenType.PRIVATE);
    }


    public Slice<MemoryResponseDto> searchMemoryUserCreatedForMyPage(Pageable pageable, Long userSeq, GroupType groupType) {

        List<OrderSpecifier> ORDERS = getAllOrderSpecifiers(pageable);

        List<MemoryResponseDto> result = query.select(Projections.constructor(MemoryResponseDto.class,
                        place.id,
                        place.placeName,
                        memory.id,
                        memory.title,
                        memory.content,
                        memory.stars,
                        createdByLoginUser(userSeq),
                        memory.visitedDate))
                .from(memory)
                .innerJoin(place).on(place.id.eq(memory.placeId))
                .where(
                        createdByLoginUser(userSeq),
                        eqGroup(groupType)
                )
                .orderBy(ORDERS.stream().toArray(OrderSpecifier[]::new))
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize() + 1)
                .fetch();

        initMemoryImageAndKeyword(result);
        return transferToSlice(pageable, result);
    }

    public MemoryResponseDto getMemoryByMemoryId(Long userSeq, Long memoryId) {

        MemoryResponseDto results = query.select(Projections.constructor(MemoryResponseDto.class,
                        place.id,
                        place.placeName,
                        memory.id,
                        memory.title,
                        memory.content,
                        memory.stars,
                        createdByLoginUser(userSeq),
                        memory.visitedDate))
                .from(memory)
                .innerJoin(place).on(place.id.eq(memory.placeId))
                .where(eqMemory(memoryId))
                .fetchFirst();

        Map<Long, List<ImageDto>> memoryImageList = findMemoryImage(toMemoryIds(List.of(results)));
        Map<Long, List<KeywordResponseDto>> keywordList = findKeywordList(
                toMemoryIds(List.of(results)));

        results.setMemoryImages(memoryImageList.get(results.getMemoryId()));
        results.setKeyword(keywordList.get(results.getMemoryId())
                .stream()
                .map(KeywordResponseDto::getKeyword)
                .collect(Collectors.toList()));

        return results;
    }


    // [장소 상세] - 다른 사람이 작성한 메모리
    public Slice<MemoryResponseDto> searchMemoryOtherCreate(Pageable pageable, Long userSeq,
                                                            Long placeId,
                                                            GroupType groupType) {

        List<OrderSpecifier> ORDERS = getAllOrderSpecifiers(pageable);

        List<MemoryResponseDto> result = query.select(
                        Projections.constructor(MemoryResponseDto.class, place.id, place.placeName,
                                memory.id, memory.title,
                                memory.content,
                                memory.stars,
                                memory.userId.eq(userSeq), memory.visitedDate))
                .from(memory)
                .leftJoin(place).on(place.id.eq(memory.placeId))
                .where(
                        eqPlace(placeId),
                        createdByNotCurrentLoginUser(userSeq), // 내가 작성자인 메모리
                        eqGroup(groupType)// 그룹 타입 필터링 용
                )
                .orderBy(ORDERS.stream().toArray(OrderSpecifier[]::new))
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize() + 1)
                .fetch();

        initMemoryImageAndKeyword(result);
        return transferToSlice(pageable, result);
    }

    // 해당 장소에 대해 내 그룹 사람들이 쓴 메모리 조회
    public Slice<MemoryResponseDto> getMyGroupMemory(Pageable pageable, Long userSeq, Long placeId,
                                                     GroupType groupType) {

        List<OrderSpecifier> ORDERS = getAllOrderSpecifiers(pageable);

        List<MemoryResponseDto> result = query.select(
                        Projections.constructor(MemoryResponseDto.class, place.id, place.placeName,
                                memory.id, memory.title,
                                memory.content,
                                memory.stars,
                                memory.userId.eq(userSeq), memory.visitedDate))
                .from(memory)
                .leftJoin(place).on(place.id.eq(memory.placeId))
                .where(
                        memory.userId.in(
                                JPAExpressions.select(groupAndUser.user.userSeq).from(groupAndUser)
                                        .where(groupAndUser.group.id.in(
                                                JPAExpressions.select(groupAndUser.group.id)
                                                        .from(groupAndUser)
                                                        .where(groupAndUser.user.userSeq.eq(
                                                                userSeq))
                                        )).distinct()
                        ),
                        eqPlace(placeId),
                        memory.userId.ne(userSeq),
                        eqGroup(groupType),
                        memory.openType.ne(OpenType.PRIVATE)
                )
                .orderBy(ORDERS.stream().toArray(OrderSpecifier[]::new))
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize() + 1)
                .fetch();

        initMemoryImageAndKeyword(result);
        return transferToSlice(pageable, result);
    }


    public HashMap<String, Long> countMemoriesBelongToPlace(Long userSeq, Long placeId) {

        Long memoriesBelongToLoginUser = query.select(memory.count())
                .from(memory)
                .where(
                        eqPlace(placeId),
                        memory.userId.eq(userSeq)
                ).fetchOne();

        Long memoriesNotBelongToLoginUser = query.select(memory.count())
                .from(memory)
                .where(
                        eqPlace(placeId),
                        memory.userId.ne(userSeq)
                ).fetchOne();

        HashMap<String, Long> map = new HashMap<>();
        map.put("belongToUser", memoriesBelongToLoginUser);
        map.put("notBelongToUser", memoriesNotBelongToLoginUser);

        return map;
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

    private Map<Long, List<KeywordResponseDto>> findKeywordList(List<Long> memoryIds) {

        List<Object[]> queryResult = em.createNativeQuery("select kt.memory_id,kt.keyword from tb_keywords_table kt where kt.memory_id in :memoryIds")
                .setParameter("memoryIds", memoryIds)
                .getResultList();

        List<KeywordResponseDto> keywordList = new ArrayList<>();

        for (Object[] objects : queryResult) {
            KeywordResponseDto keywordResponse = new KeywordResponseDto();
            keywordResponse.setMemoryId(((BigInteger) objects[0]).longValue());
            keywordResponse.setKeyword((String) objects[1]);
            keywordList.add(keywordResponse);
        }

        return keywordList.stream().collect(Collectors.groupingBy(KeywordResponseDto::getMemoryId));
    }

    private Map<Long, List<ImageDto>> findMemoryImage(List<Long> memoryIds) {

        List<ImageDto> results = query.select(Projections.constructor(ImageDto.class, memoryImage.id, memoryImage.memory.id, memoryImage.imagePath))
                .from(memoryImage)
                .where(memoryImage.memory.id.in(memoryIds))
                .fetch();

        return results.stream().collect(Collectors.groupingBy(ImageDto::getMemoryId));
    }

    private List<Long> toMemoryIds(List<MemoryResponseDto> result) {

        return result.stream().map(MemoryResponseDto::getMemoryId).collect(Collectors.toList());
    }

    private void initMemoryImageAndKeyword(List<MemoryResponseDto> results) {

        Map<Long, List<ImageDto>> memoryImageList = findMemoryImage(toMemoryIds(results));
        Map<Long, List<KeywordResponseDto>> keywordList = findKeywordList(toMemoryIds(results));

        results.forEach(memory -> {
            memory.setMemoryImages(memoryImageList.get(memory.getMemoryId()));
            memory.setKeyword(keywordList.get(memory.getMemoryId()).stream().map(KeywordResponseDto::getKeyword).collect(Collectors.toList()));
        });
    }


    private BooleanExpression eqGroup(GroupType groupType) {

        if (groupType == null || groupType == GroupType.ALL) {
            return null;
        }

        return null;
    }

    private static BooleanExpression eqPlace(Long placeId) {
        return memory.placeId.eq(placeId);
    }

    private static BooleanExpression eqMemory(Long memoryId) {
        return memory.id.eq(memoryId);
    }

    private static BooleanExpression createdByLoginUser(Long userSeq) {
        return memory.userId.eq(userSeq);
    }

    private static BooleanExpression createdByNotCurrentLoginUser(Long userSeq) {
        return memory.userId.ne(userSeq);
    }

    private static SliceImpl<MemoryResponseDto> transferToSlice(Pageable pageable, List<MemoryResponseDto> results) {
        boolean hasNext = false;

        if (results.size() > pageable.getPageSize()) {
            hasNext = true;
            results.remove(pageable.getPageSize());
        }

        return new SliceImpl<>(results, pageable, hasNext);
    }

}











