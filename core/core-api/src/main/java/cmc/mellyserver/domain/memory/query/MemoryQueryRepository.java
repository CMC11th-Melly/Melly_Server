package cmc.mellyserver.domain.memory.query;


import cmc.mellyserver.dbcore.group.enums.GroupType;
import cmc.mellyserver.dbcore.memory.enums.OpenType;
import cmc.mellyserver.domain.memory.query.dto.ImageDto;
import cmc.mellyserver.domain.memory.query.dto.KeywordDto;
import cmc.mellyserver.domain.memory.query.dto.MemoryDetailResponseDto;
import cmc.mellyserver.domain.memory.query.dto.MemoryResponseDto;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.JPAExpressions;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.SliceImpl;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import static cmc.mellyserver.dbcore.group.QGroupAndUser.groupAndUser;
import static cmc.mellyserver.dbcore.group.QUserGroup.userGroup;
import static cmc.mellyserver.dbcore.memory.QKeyword.keyword;
import static cmc.mellyserver.dbcore.memory.QMemory.memory;
import static cmc.mellyserver.dbcore.memory.QMemoryImage.memoryImage;
import static cmc.mellyserver.dbcore.place.QPlace.place;


@Repository
@RequiredArgsConstructor
public class MemoryQueryRepository {

    private JPAQueryFactory query;


    public Slice<MemoryResponseDto> searchMemoryUserCreatedForPlace(Long lastId, Pageable pageable, Long userId, Long placeId, GroupType groupType) {


        List<MemoryResponseDto> results = query.select(Projections.constructor(MemoryResponseDto.class,
                        memory.id,
                        memory.title,
                        memory.visitedDate,
                        userGroup.groupType
                ))
                .from(memory)
                .innerJoin(memoryImage).on(memoryImage.memory.id.eq(memory.id))
                .innerJoin(place).on(place.id.eq(memory.placeId)).fetchJoin()
                .innerJoin(userGroup).on(userGroup.id.eq(memory.groupId)).fetchJoin()
                .where(
                        isActive(), // =
                        ltMemoryId(lastId),
                        createdByLoginUser(userId), // =
                        eqPlace(placeId), // =
                        eqGroup(groupType) // =
                )
                .orderBy(memory.id.desc())
                .limit(pageable.getPageSize() + 1)
                .distinct()
                .fetch();

        return transferToSlice(pageable, results);
    }

    public Slice<MemoryResponseDto> getMyGroupMemory(Long lastId, Pageable pageable, Long groupId, GroupType groupType) {

        List<MemoryResponseDto> results = query.select(Projections.constructor(MemoryResponseDto.class,
                        memory.id,
                        memory.title,
                        memory.visitedDate,
                        userGroup.groupType
                ))
                .from(memory)
                .innerJoin(place).on(place.id.eq(memory.placeId))
                .innerJoin(userGroup).on(userGroup.id.eq(memory.groupId)).fetchJoin()
                .where(
                        isActive(),
                        ltMemoryId(lastId),
                        memory.groupId.eq(groupId),
                        eqGroup(groupType),
                        checkOpenTypeAllOrGroup()
                )
                .orderBy(memory.id.desc())
                .limit(pageable.getPageSize() + 1)
                .fetch();

        return transferToSlice(pageable, results);
    }

    private BooleanExpression checkOpenTypeAllOrGroup() {
        return memory.openType.ne(OpenType.PRIVATE);
    }


    // 상세 페이지 따로 가져오도록 하고, 리스트는 그냥 보여주면 되지 않나? 상셍 페이지는 그냥 캐싱 처리 해버리는것도 가능한데!
    public Slice<MemoryResponseDto> searchMemoryUserCreatedForMyPage(Long lastId, Pageable pageable, Long userId, GroupType groupType) {


        List<MemoryResponseDto> result = query.select(Projections.constructor(MemoryResponseDto.class,
                        memory.id,
                        memory.title,
                        memory.visitedDate,
                        userGroup.groupType
                ))
                .from(memory)
                .innerJoin(userGroup).on(userGroup.id.eq(memory.groupId)).fetchJoin()
                .where(
                        ltMemoryId(lastId),
                        createdByLoginUser(userId),
                        eqGroup(groupType)
                )
                .limit(pageable.getPageSize() + 1)
                .fetch();

        List<MemoryResponseDto> reversedResult = result.stream()
                .sorted((dto1, dto2) -> Long.compare(dto2.getMemoryId(), dto1.getMemoryId()))
                .collect(Collectors.toList());

        return transferToSlice(pageable, reversedResult);
    }


    public MemoryDetailResponseDto findMemoryDetail(Long memoryId) {

        return query.select(Projections.constructor(MemoryDetailResponseDto.class,
                        place.id, // 장소 ID
                        place.placeName, // 노원구 상계동
                        memory.id, // 장소 ID
                        memory.title, // 메모리 제목
                        memory.content, // 메모리 내용
                        memory.stars, // 별점
                        memory.visitedDate, // 방문 일자
                        userGroup.id,
                        userGroup.groupType,
                        userGroup.groupName,
                        userGroup.groupIcon
                ))
                .from(memory)
                .innerJoin(place).on(place.id.eq(memory.placeId))
                .innerJoin(userGroup).on(memory.groupId.eq(userGroup.id))
                .where(eqMemory(memoryId))
                .fetchOne();
    }


    // [장소 상세] - 다른 사람이 작성한 메모리
    public Slice<MemoryResponseDto> searchMemoryOtherCreate(Long lastId, Pageable pageable, Long userId, Long placeId, GroupType groupType) {

        List<MemoryResponseDto> result = query.select(Projections.constructor(MemoryResponseDto.class,
                        memory.id,
                        memory.title,
                        memory.visitedDate,
                        userGroup.groupType
                ))
                .from(memory)
                .innerJoin(userGroup).on(userGroup.id.eq(memory.groupId)).fetchJoin()
                .where(
                        isActive(),
                        ltMemoryId(lastId),
                        eqPlace(placeId),
                        createdByNotCurrentLoginUser(userId), // 내가 작성자인 메모리
                        eqGroup(groupType)// 그룹 타입 필터링 용
                )
                .orderBy(memory.id.desc())
                .limit(pageable.getPageSize() + 1)
                .fetch();

        return transferToSlice(pageable, result);
    }

    // 해당 장소에 대해 내 그룹 사람들이 쓴 메모리 조회
    public Slice<MemoryResponseDto> getMyGroupMemoryInPlace(Long lastId, Pageable pageable, Long userId, Long placeId, GroupType groupType) {

        List<MemoryResponseDto> result = query.select(Projections.constructor(MemoryResponseDto.class,
                        memory.id,
                        memory.title,
                        memoryImage.imagePath,
                        memory.visitedDate,
                        userGroup.groupType
                ))
                .from(memory)
                .leftJoin(place).on(place.id.eq(memory.placeId))
                .innerJoin(userGroup).on(userGroup.id.eq(memory.groupId)).fetchJoin()
                .where(
                        isActive(),
                        ltMemoryId(lastId),
                        inSameGroup(userId),
                        eqPlace(placeId),
                        createdByNotCurrentLoginUser(userId),
                        eqGroup(groupType),
                        memory.openType.ne(OpenType.PRIVATE)
                )
                .orderBy(memory.id.desc())
                .limit(pageable.getPageSize() + 1)
                .fetch();

        return transferToSlice(pageable, result);
    }


    public HashMap<String, Long> countMemoriesBelongToPlace(Long id, Long placeId) {

        Long memoriesBelongToLoginUser = query.select(memory.count())
                .from(memory)
                .where(
                        eqPlace(placeId),
                        memory.userId.eq(id)
                ).fetchOne();

        Long memoriesNotBelongToLoginUser = query.select(memory.count())
                .from(memory)
                .where(
                        eqPlace(placeId),
                        memory.userId.ne(id)
                ).fetchOne();

        HashMap<String, Long> map = new HashMap<>();
        map.put("belongToUser", memoriesBelongToLoginUser);
        map.put("notBelongToUser", memoriesNotBelongToLoginUser);

        return map;
    }

    public List<ImageDto> findMemoryImage(Long memoryId) {

        return query.select(Projections.constructor(ImageDto.class, memoryImage.id, memoryImage.imagePath))
                .from(memoryImage)
                .where(memoryImage.memory.id.eq(memoryId))
                .fetch();
    }

    public List<KeywordDto> findKeyword(Long memoryId) {

        return query.select(Projections.constructor(KeywordDto.class, keyword.id, keyword.content))
                .from(keyword)
                .where(keyword.memory.id.eq(memoryId))
                .fetch();
    }


    private BooleanExpression eqGroup(GroupType groupType) {

        if (Objects.isNull(groupType)) {
            return null;
        }

        return userGroup.groupType.eq(groupType);

    }

    private BooleanExpression ltMemoryId(Long memoryId) {

        if (memoryId == null) {
            return null;
        }

        return memory.id.lt(memoryId);
    }

    private static BooleanExpression eqPlace(Long placeId) {
        return memory.placeId.eq(placeId);
    }

    private static BooleanExpression eqMemory(Long memoryId) {
        return memory.id.eq(memoryId);
    }

    private static BooleanExpression inSameGroup(Long userId) {
        return memory.userId.in(JPAExpressions.select(groupAndUser.user.id).from(groupAndUser)
                .where(
                        groupAndUser.group.id.in(JPAExpressions.select(groupAndUser.group.id)
                                .from(groupAndUser)
                                .where(groupAndUser.user.id.eq(userId))
                        )).distinct());
    }

    private static BooleanExpression createdByLoginUser(Long id) {
        return memory.userId.eq(id);
    }

    private static BooleanExpression isActive() {
        return memory.is_deleted.eq(Boolean.FALSE);
    }

    private static BooleanExpression createdByNotCurrentLoginUser(Long id) {
        return memory.userId.ne(id);
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











