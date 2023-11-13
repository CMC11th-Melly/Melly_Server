package cmc.mellyserver.domain.memory.query;

import static cmc.mellyserver.dbcore.group.QGroupAndUser.*;
import static cmc.mellyserver.dbcore.group.QUserGroup.*;
import static cmc.mellyserver.dbcore.memory.QMemory.*;
import static cmc.mellyserver.dbcore.memory.QMemoryImage.*;
import static cmc.mellyserver.dbcore.place.QPlace.*;

import java.util.HashMap;
import java.util.List;
import java.util.Objects;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.SliceImpl;
import org.springframework.stereotype.Repository;

import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.JPAExpressions;
import com.querydsl.jpa.impl.JPAQueryFactory;

import cmc.mellyserver.dbcore.group.GroupType;
import cmc.mellyserver.dbcore.memory.OpenType;
import cmc.mellyserver.domain.memory.query.dto.ImageDto;
import cmc.mellyserver.domain.memory.query.dto.MemoryDetailResponseDto;
import cmc.mellyserver.domain.memory.query.dto.MemoryResponseDto;
import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class MemoryQueryRepository {

    private final JPAQueryFactory query;

    /*
    특정 장소에 속하는 유저 메모리 리스트 조회
     */
    public Slice<MemoryResponseDto> findUserMemories(Long lastId, Pageable pageable, Long userId, Long placeId,
        GroupType groupType) {

        List<MemoryResponseDto> results = query
            .select(
                Projections.constructor(MemoryResponseDto.class, memory.id, memory.title, memory.visitedDate,
                    userGroup.groupType)
            )
            .from(memory)
            .leftJoin(memoryImage).on(memoryImage.memory.id.eq(memory.id)).fetchJoin()
            .leftJoin(place).on(place.id.eq(memory.placeId)).fetchJoin()
            .leftJoin(userGroup).on(userGroup.id.eq(memory.groupId)).fetchJoin()
            .where(
                isActive(),
                ltMemoryId(lastId),
                createdByLoginUser(userId),
                eqPlace(placeId),
                eqGroup(groupType)
            )
            .orderBy(memory.id.desc())
            .limit(pageable.getPageSize() + 1)
            .distinct()
            .fetch();

        return transferToSlice(pageable, results);
    }

    public MemoryDetailResponseDto findMemoryDetail(Long memoryId) {

        return query.select(Projections.constructor(MemoryDetailResponseDto.class, place.id,

                place.name, // 노원구 상계동
                memory.id, // 장소 ID
                memory.title, // 메모리 제목
                memory.content, // 메모리 내용¬
                memory.stars, // 별점
                memory.visitedDate, // 방문 일자
                userGroup.id, userGroup.groupType, userGroup.name, userGroup.icon))
            .from(memory)
            .innerJoin(place)
            .on(place.id.eq(memory.placeId))
            .innerJoin(userGroup)
            .on(memory.groupId.eq(userGroup.id))
            .where(eqMemory(memoryId))
            .fetchOne();
    }

    // [장소 상세] - 다른 사람이 작성한 메모리
    public Slice<MemoryResponseDto> findOtherMemories(Long lastId, Pageable pageable, Long userId, Long placeId,
        GroupType groupType) {

        List<MemoryResponseDto> result = query
            .select(Projections.constructor(MemoryResponseDto.class, memory.id, memory.title, memory.visitedDate,
                userGroup.groupType))
            .from(memory)
            .leftJoin(userGroup)
            .on(userGroup.id.eq(memory.groupId))
            .fetchJoin()
            .where(isActive(),
                ltMemoryId(lastId),
                eqPlace(placeId),
                createdByNotCurrentLoginUser(userId), // 내가
                eqGroup(groupType)
            )
            .orderBy(memory.id.desc())
            .limit(pageable.getPageSize() + 1)
            .fetch();

        return transferToSlice(pageable, result);
    }

    // 해당 장소에 대해 내 그룹 사람들이 쓴 메모리 조회
    public Slice<MemoryResponseDto> findGroupMemories(Long lastId, Pageable pageable, Long userId, Long placeId,
        GroupType groupType) {

        List<MemoryResponseDto> result = query
            .select(
                Projections.constructor(MemoryResponseDto.class, memory.id, memory.title,
                    memory.visitedDate, userGroup.groupType)
            )
            .from(memory)
            .leftJoin(place).on(place.id.eq(memory.placeId))
            .leftJoin(userGroup).on(userGroup.id.eq(memory.groupId))
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

    public Slice<MemoryResponseDto> findGroupMemoriesById(Long lastId, Pageable pageable, Long groupId, Long userId,
        GroupType groupType) {

        List<MemoryResponseDto> result = query
            .select(
                Projections.constructor(MemoryResponseDto.class, memory.id, memory.title,
                    memory.visitedDate, userGroup.groupType)
            )
            .from(memory)
            .leftJoin(place).on(place.id.eq(memory.placeId))
            .leftJoin(userGroup).on(userGroup.id.eq(memory.groupId))
            .where(
                isActive(),
                ltMemoryId(lastId),
                memory.groupId.eq(groupId),
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
            .where(eqPlace(placeId), memory.userId.eq(id))
            .fetchOne();

        Long memoriesNotBelongToLoginUser = query.select(memory.count())
            .from(memory)
            .where(eqPlace(placeId), memory.userId.ne(id))
            .fetchOne();

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

    private BooleanExpression eqGroup(GroupType groupType) {

        if (Objects.isNull(groupType)) {
            return null;
        }

        return userGroup.groupType.eq(groupType);

    }

    private BooleanExpression ltMemoryId(Long memoryId) {

        if (memoryId == -1L) {
            return null;
        }

        return memory.id.lt(memoryId);
    }

    private BooleanExpression eqPlace(Long placeId) {
        if (Objects.isNull(placeId)) {
            return null;
        }

        return memory.placeId.eq(placeId);
    }

    private BooleanExpression eqMemory(Long memoryId) {
        return memory.id.eq(memoryId);
    }

    private BooleanExpression inSameGroup(Long userId) {
        return memory.userId.in(
            JPAExpressions.select(groupAndUser.user.id)
                .from(groupAndUser)
                .where(groupAndUser.group.id.in(
                    JPAExpressions.select(groupAndUser.group.id) // group list
                        .from(groupAndUser)
                        .where(groupAndUser.user.id.eq(userId)) // 내가 속한 그룹 리스트 조회
                        .distinct()
                )).distinct()
        );
    }

    private BooleanExpression createdByLoginUser(Long id) {
        return memory.userId.eq(id);
    }

    private BooleanExpression isActive() {
        return memory.deletedAt.isNull();
    }

    private BooleanExpression createdByNotCurrentLoginUser(Long id) {
        return memory.userId.ne(id);
    }

    private SliceImpl<MemoryResponseDto> transferToSlice(Pageable pageable, List<MemoryResponseDto> results) {
        boolean hasNext = false;

        if (results.size() > pageable.getPageSize()) {
            hasNext = true;
            results.remove(pageable.getPageSize());
        }

        return new SliceImpl<>(results, pageable, hasNext);
    }
}
