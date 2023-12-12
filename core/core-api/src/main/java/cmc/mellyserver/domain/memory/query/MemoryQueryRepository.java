package cmc.mellyserver.domain.memory.query;

import static cmc.mellyserver.dbcore.group.QGroupAndUser.*;
import static cmc.mellyserver.dbcore.group.QUserGroup.*;
import static cmc.mellyserver.dbcore.memory.memory.QMemory.*;
import static cmc.mellyserver.dbcore.memory.memory.QMemoryImage.*;
import static cmc.mellyserver.dbcore.place.QPlace.*;

import java.util.HashMap;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.JPAExpressions;
import com.querydsl.jpa.impl.JPAQueryFactory;

import cmc.mellyserver.dbcore.group.GroupType;
import cmc.mellyserver.dbcore.memory.memory.Memory;
import cmc.mellyserver.dbcore.memory.memory.MemoryImage;
import cmc.mellyserver.dbcore.memory.memory.OpenType;
import cmc.mellyserver.domain.memory.dto.response.MemoryListResponse;
import cmc.mellyserver.domain.memory.query.dto.MemoryListResponseDto;
import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class MemoryQueryRepository {

    private final JPAQueryFactory query;

    /*
    특정 장소에 속하는 유저 메모리 리스트 조회
     */
    public MemoryListResponse findUserMemories(Long lastId, Pageable pageable, Long userId, Long placeId,
        GroupType groupType) {

        List<Memory> results = query
            .select(memory)
            .from(memory)
            .leftJoin(place).on(place.id.eq(memory.placeId)).fetchJoin()
            .leftJoin(userGroup).on(userGroup.id.eq(memory.groupId)).fetchJoin()
            .leftJoin(memoryImage).on(memoryImage.memory.id.eq(memory.id))
            .where(
                isActive(),
                ltMemoryId(lastId),
                createdByLoginUser(userId),
                eqPlace(placeId),
                eqGroupType(groupType)
            )
            .orderBy(memory.id.desc())
            .limit(pageable.getPageSize() + 1)
            .distinct()
            .fetch();

        return transfer(pageable, results);
    }

    // [장소 상세] - 다른 사람이 작성한 메모리
    public MemoryListResponse findOtherMemories(Long lastId, Pageable pageable, Long userId, Long placeId,
        GroupType groupType) {

        List<Memory> result = query
            .select(memory)
            .from(memory)
            .leftJoin(place).on(place.id.eq(memory.placeId)).fetchJoin()
            .leftJoin(userGroup).on(userGroup.id.eq(memory.groupId)).fetchJoin()
            .leftJoin(memoryImage).on(memoryImage.memory.id.eq(memory.id))
            .where(
                isActive(),
                ltMemoryId(lastId),
                eqPlace(placeId),
                createdByOtherUser(userId),
                eqGroupType(groupType)
            )
            .orderBy(memory.id.desc())
            .limit(pageable.getPageSize() + 1)
            .fetch();

        return transfer(pageable, result);
    }

    // 해당 장소에 대해 내 그룹 사람들이 쓴 메모리 조회
    public MemoryListResponse findGroupMemories(Long lastId, Pageable pageable, Long userId, Long placeId) {

        List<Memory> result = query
            .select(memory)
            .distinct()
            .from(memory)
            .leftJoin(place).on(place.id.eq(memory.placeId)).fetchJoin()
            .leftJoin(userGroup).on(userGroup.id.eq(memory.groupId)).fetchJoin()
            .leftJoin(memoryImage).on(memoryImage.memory.id.eq(memory.id))
            .where(
                ltMemoryId(lastId), // 커서 페이징
                isActive(), // 삭제 처리 안된 메모리
                notPrivate(), // 공개 & 그룹 메모리
                inSameGroup(userId), // 나와 같은 그룹의 사람들
                eqPlace(placeId) // 같은 장소
            )
            .orderBy(memory.id.desc())
            .limit(pageable.getPageSize() + 1)
            .fetch();

        return transfer(pageable, result);
    }

    public MemoryListResponse findGroupMemoriesById(Long lastId, Pageable pageable, Long groupId) {

        List<Memory> result = query
            .select(memory)
            .from(memory)
            .leftJoin(place).on(place.id.eq(memory.placeId)).fetchJoin()
            .leftJoin(userGroup).on(userGroup.id.eq(memory.groupId)).fetchJoin()
            .leftJoin(memoryImage).on(memoryImage.memory.id.eq(memory.id))
            .where(
                ltMemoryId(lastId), // 커서 페이징
                isActive(), // 삭제 처리 되지 않은 메모리
                notPrivate(), // 공개 or 그룹 메모리
                eqGroup(groupId) // 같은 그룹
            )
            .orderBy(memory.id.desc())
            .limit(pageable.getPageSize() + 1)
            .fetch();

        return transfer(pageable, result);
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
        map.put("myMemory", memoriesBelongToLoginUser);
        map.put("otherMemory", memoriesNotBelongToLoginUser);

        return map;
    }

    private BooleanExpression eqGroupType(GroupType groupType) {

        if (Objects.isNull(groupType)) {
            return null;
        }

        return userGroup.groupType.eq(groupType);

    }

    private BooleanExpression notPrivate() {
        return memory.openType.ne(OpenType.PRIVATE);
    }

    private BooleanExpression eqGroup(Long groupId) {
        return memory.groupId.eq(groupId);
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

    private BooleanExpression inSameGroup(Long userId) {
        return memory.userId.in(
            JPAExpressions.select(groupAndUser.user.id)
                .from(groupAndUser)
                .where(groupAndUser.group.id.in(
                    JPAExpressions.select(groupAndUser.group.id)
                        .from(groupAndUser)
                        .where(groupAndUser.user.id.eq(userId))
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

    private BooleanExpression createdByOtherUser(Long id) {
        return memory.userId.ne(id);
    }

    private MemoryListResponse transfer(Pageable pageable, List<Memory> results) {

        boolean hasNext = checkLastPage(pageable, results);

        List<MemoryListResponseDto> result = results.stream().map(memory -> {
            List<MemoryImage> memoryImages = memory.getMemoryImages();
            String imageUrl = getFirstImage(memoryImages);
            return new MemoryListResponseDto(memory.getId(), memory.getTitle(), imageUrl, memory.getVisitedDate());
        }).collect(Collectors.toList());

        return new MemoryListResponse(result, hasNext);
    }

    private boolean checkLastPage(Pageable pageable, List<Memory> results) {

        boolean hasNext = false;

        if (results.size() > pageable.getPageSize()) {
            hasNext = true;
            results.remove(pageable.getPageSize());
        }

        return hasNext;
    }

    private String getFirstImage(List<MemoryImage> memoryImages) {

        if (memoryImages.isEmpty()) {
            return null;
        }

        return memoryImages.get(0).getImagePath();
    }
}
