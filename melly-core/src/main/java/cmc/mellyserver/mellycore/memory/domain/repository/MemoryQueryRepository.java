package cmc.mellyserver.mellycore.memory.domain.repository;

import static cmc.mellyserver.mellycore.group.domain.QGroupAndUser.*;
import static cmc.mellyserver.mellycore.memory.domain.QMemory.*;
import static cmc.mellyserver.mellycore.memory.domain.QMemoryImage.*;
import static cmc.mellyserver.mellycore.place.domain.QPlace.*;
import static org.springframework.util.ObjectUtils.*;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.persistence.EntityManager;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.SliceImpl;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Repository;

import com.querydsl.core.types.Order;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.JPAExpressions;
import com.querydsl.jpa.impl.JPAQueryFactory;

import cmc.mellyserver.mellycore.common.enums.GroupType;
import cmc.mellyserver.mellycore.common.enums.OpenType;
import cmc.mellyserver.mellycore.common.util.jpa.QueryDslUtil;
import cmc.mellyserver.mellycore.memory.domain.repository.dto.FindPlaceInfoByMemoryNameResponseDto;
import cmc.mellyserver.mellycore.memory.domain.repository.dto.ImageDto;
import cmc.mellyserver.mellycore.memory.domain.repository.dto.KeywordResponseDto;
import cmc.mellyserver.mellycore.memory.domain.repository.dto.MemoryResponseDto;
import cmc.mellyserver.mellycore.user.domain.User;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Repository
public class MemoryQueryRepository {

	private final EntityManager em;
	private final JPAQueryFactory query;

	private static SliceImpl<MemoryResponseDto> transferToSlice(Pageable pageable, List<MemoryResponseDto> results) {
		boolean hasNext = false;

		if (results.size() > pageable.getPageSize()) {
			hasNext = true;
			results.remove(pageable.getPageSize());
		}

		return new SliceImpl<>(results, pageable, hasNext);
	}

	public List<FindPlaceInfoByMemoryNameResponseDto> searchPlaceByContainMemoryName(Long userSeq, String memoryName) {

		return query.select(
				Projections.constructor(FindPlaceInfoByMemoryNameResponseDto.class, memory.placeId, memory.title))
			.from(memory)
			.where(
				memory.userId.eq(userSeq),  // 본인이 가지고 있는 메모리
				memory.isReported.isFalse(),      // 신고되지 않은 메모리
				memory.isDelete.isFalse(),        // 삭제 되지 않은 메모리
				memory.title.contains(memoryName)) // 메모리 제목으로 검색하는 로직
			.distinct().fetch();
	}

	// [장소 상세] - 나의 메모리, 마이페이지 - 내가 작성한 메모리 조회 (최적화 완료, 인덱스 추가 필요)
	public Slice<MemoryResponseDto> searchMemoryUserCreatedForPlace(Pageable pageable, Long userSeq, Long placeId,
		GroupType groupType) {

		List<OrderSpecifier> ORDERS = getAllOrderSpecifiers(pageable);

		List<MemoryResponseDto> results = query.select(
				Projections.constructor(MemoryResponseDto.class, place.id, place.placeName, memory.id, memory.title,
					memory.content, memory.groupInfo.groupType, memory.groupInfo.groupName, memory.stars,
					memory.userId.eq(userSeq), memory.visitedDate))
			.from(memory)
			.leftJoin(place).on(place.id.eq(memory.placeId))
			.where(
				memory.userId.eq(userSeq), // 로그인한 유저가 작성자인 메모리 조회
				eqGroup(groupType), // 그룹 타입 필터링
				memory.placeId.eq(placeId) // 특정 장소에 속한 메모리 조회
			)
			.orderBy(ORDERS.stream().toArray(OrderSpecifier[]::new))
			.offset(pageable.getOffset())
			.limit(pageable.getPageSize() + 1)
			.fetch();

		initMemoryImageAndKeyword(results);
		return transferToSlice(pageable, results);
	}

	// [마이 페이지] - 내가 작성한 메모리 목록 조회
	public Slice<MemoryResponseDto> searchMemoryUserCreatedForMyPage(Pageable pageable, Long userSeq,
		GroupType groupType) {

		List<OrderSpecifier> ORDERS = getAllOrderSpecifiers(pageable);

		List<MemoryResponseDto> result = query.select(
				Projections.constructor(MemoryResponseDto.class, place.id, place.placeName, memory.id, memory.title,
					memory.content, memory.groupInfo.groupType, memory.groupInfo.groupName, memory.stars,
					memory.userId.eq(userSeq), memory.visitedDate))
			.from(memory)
			.leftJoin(place).on(place.id.eq(memory.placeId))
			.where(
				memory.userId.eq(userSeq), // 내가 작성자인 메모리
				eqGroup(groupType)// 그룹 타입 필터링 용
			)
			.orderBy(ORDERS.stream().toArray(OrderSpecifier[]::new))
			.offset(pageable.getOffset())
			.limit(pageable.getPageSize() + 1)
			.fetch();

		initMemoryImageAndKeyword(result);
		return transferToSlice(pageable, result);
	}

	public MemoryResponseDto getMemoryByMemoryId(Long userSeq, Long memoryId) {

		MemoryResponseDto results = query.select(
				Projections.constructor(MemoryResponseDto.class, place.id, place.placeName, memory.id, memory.title,
					memory.content, memory.groupInfo.groupType, memory.groupInfo.groupName, memory.stars,
					memory.userId.eq(userSeq), memory.visitedDate))
			.from(memory)
			.leftJoin(place).on(place.id.eq(memory.placeId))
			.where(memory.id.eq(memoryId))
			.fetchFirst();

		Map<Long, List<ImageDto>> memoryImageList = findMemoryImage(toMemoryIds(List.of(results)));
		Map<Long, List<KeywordResponseDto>> keywordList = findKeywordList(toMemoryIds(List.of(results)));

		results.setMemoryImages(memoryImageList.get(results.getMemoryId()));
		results.setKeyword(keywordList.get(results.getMemoryId())
			.stream()
			.map(KeywordResponseDto::getKeyword)
			.collect(Collectors.toList()));

		return results;
	}

	// [장소 상세] - 다른 사람이 작성한 메모리
	public Slice<MemoryResponseDto> searchMemoryOtherCreate(Pageable pageable, Long userSeq, Long placeId,
		GroupType groupType) {

		List<OrderSpecifier> ORDERS = getAllOrderSpecifiers(pageable);

		List<MemoryResponseDto> results = query.select(
				Projections.constructor(MemoryResponseDto.class, place.id, place.placeName, memory.id, memory.title,
					memory.content, memory.groupInfo.groupType, memory.groupInfo.groupName, memory.stars,
					memory.userId.eq(userSeq), memory.visitedDate)) //  다른 사람이 작성한 메모리 가져오기
			.from(memory)
			.leftJoin(place).on(place.id.eq(memory.placeId))
			.where(
				memory.placeId.eq(placeId),  // 특정 장소에 속해있는지 체크
				memory.userId.ne(userSeq), // 로그인 유저가 아닌 다른 사람이 작성했는지 체크
				eqGroup(groupType), // 그룹 타입 체크
				memory.isReported.isFalse(), // 신고되지 않은 메모리 조회
				memory.isDelete.isFalse(),   // 삭제되지 않은 메모리 조회
				memory.openType.eq(OpenType.ALL) // 다른 사람이 전체 공개로 올린 메모리만 보여주기
			)
			.orderBy(ORDERS.stream().toArray(OrderSpecifier[]::new))
			.offset(pageable.getOffset())
			.limit(pageable.getPageSize() + 1)
			.fetch();

		initMemoryImageAndKeyword(results);
		return transferToSlice(pageable, results);
	}

	// 해당 장소에 대해 내 그룹 사람들이 쓴 메모리 조회
	public Slice<MemoryResponseDto> getMyGroupMemory(Pageable pageable, Long userSeq, Long placeId,
		GroupType groupType) {

		List<OrderSpecifier> ORDERS = getAllOrderSpecifiers(pageable);

		List<MemoryResponseDto> results = query.select(
				Projections.constructor(MemoryResponseDto.class, place.id, place.placeName, memory.id, memory.title,
					memory.content, memory.groupInfo.groupType, memory.groupInfo.groupName, memory.stars,
					memory.userId.eq(userSeq), memory.visitedDate))
			.from(memory)
			.leftJoin(place).on(place.id.eq(memory.placeId))
			.where(
				memory.userId.in(
					JPAExpressions.select(groupAndUser.user.userSeq).from(groupAndUser)
						.where(groupAndUser.group.id.in(
							JPAExpressions.select(groupAndUser.group.id).from(groupAndUser)
								.where(groupAndUser.user.userSeq.eq(userSeq))
						)).distinct()
				),
				memory.placeId.eq(placeId),
				eqGroup(groupType),
				memory.openType.ne(OpenType.PRIVATE)
			)
			.orderBy(ORDERS.stream().toArray(OrderSpecifier[]::new))
			.offset(pageable.getOffset())
			.limit(pageable.getPageSize() + 1)
			.fetch();

		initMemoryImageAndKeyword(results);
		return transferToSlice(pageable, results);
	}

	public HashMap<String, Long> countMemoryOfPlace(Long placeId, Long userSeq) {

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
			)
			.fetchOne();

		HashMap<String, Long> map = new HashMap<>();
		map.put("myMemoryCount", myMemoryCount);
		map.put("otherMemoryCount", otherMemoryCount);

		return map;
	}

	public HashMap<String, Long> countMemoriesBelongToPlace(Long userSeq, Long placeId) {

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
		map.put("belongToUser", memoriesBelongToLoginUser);
		map.put("notBelongToUser", memoriesNotBelongToLoginUser);

		return map;
	}

	private BooleanExpression eqGroup(GroupType groupType) {

		if (groupType == null || groupType == GroupType.ALL) {
			return null;
		}

		return memory.groupInfo.groupType.eq(groupType);
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

		List<Object[]> queryResult = em.createNativeQuery(
				"select kt.memory_id,kt.keyword from tb_keywords_table kt where kt.memory_id in :memoryIds")
			.setParameter("memoryIds", memoryIds)
			.getResultList();

		List<KeywordResponseDto> keywordList = new ArrayList<>();

		for (Object[] objects : queryResult) {
			KeywordResponseDto keywordResponse = new KeywordResponseDto();
			keywordResponse.setMemoryId(((BigInteger)objects[0]).longValue());
			keywordResponse.setKeyword((String)objects[1]);
			keywordList.add(keywordResponse);
		}

		return keywordList.stream()
			.collect(Collectors.groupingBy(KeywordResponseDto::getMemoryId));
	}

	private Map<Long, List<ImageDto>> findMemoryImage(List<Long> memoryIds) {

		List<ImageDto> results = query.select(
				Projections.constructor(ImageDto.class, memoryImage.id, memoryImage.memory.id, memoryImage.imagePath))
			.from(memoryImage)
			.where(memoryImage.memory.id.in(memoryIds))
			.fetch();

		return results.stream()
			.collect(Collectors.groupingBy(ImageDto::getMemoryId));
	}

	private List<Long> toMemoryIds(List<MemoryResponseDto> result) {
		return result.stream()
			.map(o -> o.getMemoryId())
			.collect(Collectors.toList());
	}

	private void initMemoryImageAndKeyword(List<MemoryResponseDto> results) {
		Map<Long, List<ImageDto>> memoryImageList = findMemoryImage(toMemoryIds(results));
		Map<Long, List<KeywordResponseDto>> keywordList = findKeywordList(toMemoryIds(results)); // 키워드 모음

		results.forEach(f -> {
			f.setMemoryImages(memoryImageList.get(f.getMemoryId()));
			f.setKeyword(keywordList.get(f.getMemoryId())
				.stream()
				.map(KeywordResponseDto::getKeyword)
				.collect(Collectors.toList()));
		});
	}

	private BooleanExpression eqPlace(Long placeId) {
		if (placeId == null) {
			return null;
		}
		return memory.placeId.eq(placeId);
	}

	private BooleanExpression eqUserId(Long userSeq) {
		if (userSeq == null) {
			return null;
		}
		return memory.userId.eq(userSeq);
	}

	private BooleanExpression neUser(User user) {
		if (user == null) {
			return null;
		}
		return memory.userId.ne(user.getUserSeq());
	}

	private BooleanExpression neUserId(Long userSeq) {
		if (userSeq == null) {
			return null;
		}
		return memory.userId.ne(userSeq);
	}

}











