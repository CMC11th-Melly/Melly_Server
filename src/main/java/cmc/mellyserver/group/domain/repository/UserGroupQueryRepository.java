package cmc.mellyserver.group.domain.repository;

import cmc.mellyserver.common.enums.OpenType;
import cmc.mellyserver.common.util.jpa.QueryDslUtil;
import cmc.mellyserver.group.domain.GroupAndUser;
import cmc.mellyserver.group.domain.UserGroup;
import cmc.mellyserver.memory.application.dto.response.GroupListForSaveMemoryResponseDto;
import cmc.mellyserver.memory.domain.MemoryImage;
import cmc.mellyserver.memory.infrastructure.data.dto.KeywordResponse;
import cmc.mellyserver.memory.infrastructure.data.dto.MemoryResponseDto;
import cmc.mellyserver.memory.presentation.dto.common.ImageDto;
import cmc.mellyserver.user.domain.User;
import cmc.mellyserver.user.presentation.dto.common.UserDto;
import cmc.mellyserver.user.presentation.dto.response.GroupLoginUserParticipatedResponseDto;
import com.querydsl.core.types.Order;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.jetbrains.annotations.NotNull;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Repository;
import javax.persistence.EntityManager;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import static cmc.mellyserver.group.domain.QGroupAndUser.groupAndUser;
import static cmc.mellyserver.group.domain.QUserGroup.userGroup;
import static cmc.mellyserver.memory.domain.QMemory.memory;
import static cmc.mellyserver.memory.domain.QMemoryImage.memoryImage;
import static cmc.mellyserver.place.domain.QPlace.place;
import static cmc.mellyserver.user.domain.QUser.user;
import static org.springframework.util.ObjectUtils.isEmpty;

@Repository
public class UserGroupQueryRepository {


    private final EntityManager em;
    private final JPAQueryFactory query;


    public UserGroupQueryRepository(EntityManager em)
    {
        this.em = em;
        this.query = new JPAQueryFactory(em);
    }


    public Slice<MemoryResponseDto> getMyGroupMemory(Pageable pageable, Long groupId, Long userSeq) {

        List<OrderSpecifier> ORDERS = getAllOrderSpecifiers(pageable);

        List<MemoryResponseDto> results = query.select(Projections.constructor(MemoryResponseDto.class, place.id, place.placeName,
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
    public List<GroupListForSaveMemoryResponseDto> getUserGroupListForMemoryEnroll(Long userSeq)
    {
        return query.select(Projections.constructor(GroupListForSaveMemoryResponseDto.class,userGroup.id,userGroup.groupName,userGroup.groupType))
                .from(groupAndUser)
                .join(groupAndUser.group,userGroup)
                .where(groupAndUser.user.userSeq.eq(userSeq))
                .fetch();
    }


    public List<GroupLoginUserParticipatedResponseDto> getGroupListLoginUserParticipate(Long userSeq)
    {
        List<GroupAndUser> results = query.select(groupAndUser)
                .from(groupAndUser)
                .join(groupAndUser.group, userGroup).fetchJoin()
                .join(groupAndUser.user, user).fetchJoin()
                .where(
                        groupAndUser.user.userSeq.eq(userSeq)
                )
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
            GroupLoginUserParticipatedResponseDto getUserGroupResponse = new GroupLoginUserParticipatedResponseDto(ga.getId(), ga.getGroup().getGroupIcon(), ga.getGroup().getGroupName(), ga.getGroup().getGroupType(), ga.getGroup().getInviteLink());
            List<User> users = map.get(getUserGroupResponse.getGroupId());
            List<UserDto> collect = users.stream().map(u -> new UserDto(u.getUserSeq(), u.getProfileImage(), u.getNickname(), u.getUserSeq().equals(userSeq))).collect(Collectors.toList());
            getUserGroupResponse.setUsers(collect);
            return getUserGroupResponse;
        }).collect(Collectors.toList());
    }


    private BooleanExpression eqUser(Long userSeq) {
        if(userSeq == null || userSeq == -1)
        {
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


    private Map<Long, List<KeywordResponse>> findKeywordList(List<Long> memoryIds) {
        List<Object[]> queryResult = em.createNativeQuery("select kt.memory_id,kt.keyword from keywords_table kt where kt.memory_id in :memoryIds")
                .setParameter("memoryIds", memoryIds)
                .getResultList();

        List<KeywordResponse> keywordList = new ArrayList<>();

        for (Object[] objects : queryResult) {
            KeywordResponse keywordResponse = new KeywordResponse();
            keywordResponse.setMemoryId(((BigInteger) objects[0]).longValue());
            keywordResponse.setKeyword((String) objects[1]);
            keywordList.add(keywordResponse);
        }

        return keywordList.stream()
                .collect(Collectors.groupingBy(KeywordResponse::getMemoryId));
    }


    private Map<Long, List<MemoryImage>> findMemoryImage(List<Long> memoryIds) {

        List<MemoryImage> results = query.select(Projections.constructor(MemoryImage.class, memoryImage.memory.id, memoryImage.id, memoryImage.imagePath))
                .from(memoryImage)
                .where(memoryImage.memory.id.in(memoryIds))
                .fetch();

        return results.stream()
                .collect(Collectors.groupingBy(MemoryImage::getId));
    }


    private List<Long> toMemoryIds(List<MemoryResponseDto> result) {
        return result.stream()
                .map(o -> o.getMemoryId())
                .collect(Collectors.toList());
    }


    @NotNull
    private static SliceImpl<MemoryResponseDto> transferToSlice(Pageable pageable, List<MemoryResponseDto> results) {
        boolean hasNext = false;

        if (results.size() > pageable.getPageSize()) {
            hasNext = true;
            results.remove(pageable.getPageSize());
        }

        return new SliceImpl<>(results, pageable, hasNext);
    }


    private void initMemoryImageAndKeyword(List<MemoryResponseDto> results) {
        Map<Long, List<MemoryImage>> memoryImageList = findMemoryImage(toMemoryIds(results));
        Map<Long, List<KeywordResponse>> keywordList = findKeywordList(toMemoryIds(results));

        results.forEach(f -> {
            f.setMemoryImages(memoryImageList.get(f.getMemoryId()).stream().map(mi -> new ImageDto(mi.getId(),mi.getImagePath())).collect(Collectors.toList()));
            f.setKeyword(keywordList.get(f.getMemoryId()).stream().map(KeywordResponse::getKeyword).collect(Collectors.toList()));
        });
    }
}