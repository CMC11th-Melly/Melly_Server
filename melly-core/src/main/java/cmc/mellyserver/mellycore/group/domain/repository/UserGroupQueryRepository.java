package cmc.mellyserver.mellycore.group.domain.repository;

import static cmc.mellyserver.mellycore.group.domain.QGroupAndUser.groupAndUser;
import static cmc.mellyserver.mellycore.group.domain.QUserGroup.userGroup;
import static cmc.mellyserver.mellycore.memory.domain.QMemory.memory;
import static cmc.mellyserver.mellycore.memory.domain.QMemoryImage.memoryImage;
import static cmc.mellyserver.mellycore.place.domain.QPlace.place;
import static cmc.mellyserver.mellycore.user.domain.QUser.user;
import static org.springframework.util.ObjectUtils.isEmpty;

import cmc.mellyserver.mellycore.common.enums.OpenType;
import cmc.mellyserver.mellycore.common.util.jpa.QueryDslUtil;
import cmc.mellyserver.mellycore.group.domain.GroupAndUser;
import cmc.mellyserver.mellycore.group.domain.UserGroup;
import cmc.mellyserver.mellycore.group.domain.repository.dto.GroupListForSaveMemoryResponseDto;
import cmc.mellyserver.mellycore.group.domain.repository.dto.GroupLoginUserParticipatedResponseDto;
import cmc.mellyserver.mellycore.memory.domain.repository.dto.ImageDto;
import cmc.mellyserver.mellycore.memory.domain.repository.dto.KeywordResponseDto;
import cmc.mellyserver.mellycore.memory.domain.repository.dto.MemoryResponseDto;
import cmc.mellyserver.mellycore.user.domain.User;
import cmc.mellyserver.mellycore.user.domain.repository.UserDto;
import com.querydsl.core.types.Order;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import javax.persistence.EntityManager;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.SliceImpl;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Repository;

@Repository
public class UserGroupQueryRepository {

    private final EntityManager em;
    private final JPAQueryFactory query;

    public UserGroupQueryRepository(EntityManager em) {
        this.em = em;
        this.query = new JPAQueryFactory(em);
    }


    public Slice<MemoryResponseDto> getMyGroupMemory(Pageable pageable, Long groupId,
            Long userSeq) {

        List<OrderSpecifier> ORDERS = getAllOrderSpecifiers(pageable);

        List<MemoryResponseDto> results = query.select(
                        Projections.constructor(MemoryResponseDto.class, place.id, place.placeName,
                                memory.id, memory.title, memory.content,
                                memory.groupInfo.groupType, memory.groupInfo.groupName,
                                memory.stars, eqUser(userSeq), memory.visitedDate))
                .from(memory)
                .leftJoin(place).on(place.id.eq(memory.placeId))
                .where(
                        memory.groupInfo.groupId.eq(groupId),  // 지금 그룹에 속한 케이스
                        checkOpenTypeAllOrGroup() // 비공개가 아닌 케이스
                )
                .offset(pageable.getOffset())
                .orderBy(ORDERS.stream().toArray(OrderSpecifier[]::new))
                .limit(pageable.getPageSize() + 1)
                .fetch();

        initMemoryImageAndKeyword(results);
        return transferToSlice(pageable, results);
    }

    // ok
    public List<GroupListForSaveMemoryResponseDto> getUserGroupListForMemoryEnroll(Long userSeq) {
        return query.select(
                        Projections.constructor(GroupListForSaveMemoryResponseDto.class, userGroup.id,
                                userGroup.groupName,
                                userGroup.groupType))
                .from(groupAndUser)
                .join(groupAndUser.group, userGroup)
                .where(groupAndUser.user.userSeq.eq(userSeq))
                .fetch();
    }

    public List<GroupLoginUserParticipatedResponseDto> getGroupListLoginUserParticipate(
            Long userSeq) {
        List<GroupAndUser> results = query.select(groupAndUser)
                .from(groupAndUser)
                .join(groupAndUser.group, userGroup).fetchJoin()
                .join(groupAndUser.user, user).fetchJoin()
                .where(groupAndUser.user.userSeq.eq(userSeq))
                .fetch();

        LinkedHashMap<Long, List<User>> map = new LinkedHashMap<>();

        for (GroupAndUser eachGroupAndUser : results) {

            UserGroup group = eachGroupAndUser.getGroup();

            if (map.containsKey(group.getId())) {
                List<User> users = map.get(group.getId());
                users.add(eachGroupAndUser.getUser());
                map.replace(group.getId(), users);
            } else {
                List<User> users = new ArrayList<>();
                users.add(eachGroupAndUser.getUser());
                map.put(group.getId(), users);
            }
        }

        return results.stream().map(ga -> {
            GroupLoginUserParticipatedResponseDto getUserGroupResponse = new GroupLoginUserParticipatedResponseDto(
                    ga.getGroup().getId(), ga.getGroup().getGroupIcon(),
                    ga.getGroup().getGroupName(),
                    ga.getGroup().getGroupType(),
                    ga.getGroup().getInviteLink());
            List<User> users = map.get(getUserGroupResponse.getGroupId());
            List<UserDto> collect = users.stream()
                    .map(u -> new UserDto(u.getUserSeq(), u.getProfileImage(), u.getNickname(),
                            u.getUserSeq().equals(userSeq)))
                    .collect(Collectors.toList());
            getUserGroupResponse.setUsers(collect);
            return getUserGroupResponse;
        }).collect(Collectors.toList());
    }

    private BooleanExpression eqUser(Long userSeq) {
        if (userSeq == null || userSeq == -1) {
            return null;
        }
        return memory.userId.eq(userSeq);
    }

    private BooleanExpression checkOpenTypeAllOrGroup() {
        return memory.openType.ne(OpenType.PRIVATE);
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
            keywordResponse.setMemoryId(((BigInteger) objects[0]).longValue());
            keywordResponse.setKeyword((String) objects[1]);
            keywordList.add(keywordResponse);
        }

        return keywordList.stream()
                .collect(Collectors.groupingBy(KeywordResponseDto::getMemoryId));
    }

    private Map<Long, List<ImageDto>> findMemoryImage(List<Long> memoryIds) {

        List<ImageDto> results = query.select(
                        Projections.constructor(ImageDto.class, memoryImage.id, memoryImage.memory.id,
                                memoryImage.imagePath))
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
        Map<Long, List<KeywordResponseDto>> keywordList = findKeywordList(toMemoryIds(results));

        results.forEach(f -> {
            f.setMemoryImages(memoryImageList.get(f.getMemoryId()));
            f.setKeyword(keywordList.get(f.getMemoryId())
                    .stream()
                    .map(KeywordResponseDto::getKeyword)
                    .collect(Collectors.toList()));
        });
    }

    private static SliceImpl<MemoryResponseDto> transferToSlice(Pageable pageable,
            List<MemoryResponseDto> results) {
        boolean hasNext = false;

        if (results.size() > pageable.getPageSize()) {
            hasNext = true;
            results.remove(pageable.getPageSize());
        }

        return new SliceImpl<>(results, pageable, hasNext);
    }
}