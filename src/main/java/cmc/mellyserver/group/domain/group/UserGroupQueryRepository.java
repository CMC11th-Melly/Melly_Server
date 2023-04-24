package cmc.mellyserver.group.domain.group;

import cmc.mellyserver.common.enums.OpenType;
import cmc.mellyserver.common.util.jpa.QueryDslUtil;
import cmc.mellyserver.group.domain.GroupAndUser;
import cmc.mellyserver.group.domain.group.dto.MyGroupMemoryResponseDto;
import cmc.mellyserver.memory.application.dto.MemoryFormGroupResponse;
import cmc.mellyserver.user.domain.User;
import cmc.mellyserver.user.presentation.dto.common.UserDto;
import cmc.mellyserver.user.presentation.dto.response.GetUserGroupResponse;
import com.querydsl.core.types.Order;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Repository;
import javax.persistence.EntityManager;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.stream.Collectors;
import static cmc.mellyserver.group.domain.QGroupAndUser.groupAndUser;
import static cmc.mellyserver.group.domain.group.QUserGroup.userGroup;
import static cmc.mellyserver.memory.domain.QMemory.memory;
import static cmc.mellyserver.place.domain.QPlace.place;
import static cmc.mellyserver.user.domain.QUser.user;
import static org.springframework.util.ObjectUtils.isEmpty;

@Repository
public class UserGroupQueryRepository {


    private final EntityManager em;
    private final JPAQueryFactory query;

    // 인텔리제이 인식 오류, 무시하고 진행하면 된다.
    public UserGroupQueryRepository(EntityManager em)
    {
        this.em = em;
        this.query = new JPAQueryFactory(em);
    }


    // TODO : 다시 안봐
    public Slice<MyGroupMemoryResponseDto> getMyGroupMemory(Pageable pageable, Long groupId, Long userSeq) {

        List<OrderSpecifier> ORDERS = getAllOrderSpecifiers(pageable);

        List<MyGroupMemoryResponseDto> results = query.select(Projections.constructor(MyGroupMemoryResponseDto.class, place.id, place.placeName,
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

        boolean hasNext = false;

        if (results.size() > pageable.getPageSize()) {
            hasNext = true;
            results.remove(pageable.getPageSize());
        }

        return new SliceImpl<>(results, pageable,hasNext);
    }

    // TODO : 찐막 검증 완료
    public List<MemoryFormGroupResponse> getUserGroupListForMemoryEnroll(Long userSeq)
    {
        return query.select(Projections.constructor(MemoryFormGroupResponse.class,userGroup.id,userGroup.groupName,userGroup.groupType))
                .from(groupAndUser)
                .join(groupAndUser.group,userGroup)
                .where(groupAndUser.user.userSeq.eq(userSeq))
                .fetch();
    }

    // 내가 속해있는 그룹을 모두 가져오고, 거기에 대한 정보들 빼내기
    // TODO : 다시 안봐
    public List<GetUserGroupResponse> getgroupInfo(Long userSeq)
    {
        List<GroupAndUser> fetch = query.select(groupAndUser)
                .from(groupAndUser)
                .join(groupAndUser.group, userGroup).fetchJoin()
                .join(groupAndUser.user, user).fetchJoin()
                .where(
                        groupAndUser.user.userSeq.eq(userSeq)
                )
                .fetch();

        LinkedHashMap<Long, List<User>> map = new LinkedHashMap<>();

        for (GroupAndUser groupAndUser1 : fetch) {
            UserGroup group = groupAndUser1.getGroup();

            if (map.containsKey(group.getId())) {
                List<User> users = map.get(group.getId());
                users.add(groupAndUser1.getUser());
                map.replace(group.getId(), users);
            } else {
                List<User> users = new ArrayList<>();
                users.add(groupAndUser1.getUser());
                map.put(group.getId(), users);
            }
        }

        return fetch.stream().map(ga -> {
            GetUserGroupResponse getUserGroupResponse = new GetUserGroupResponse(ga.getId(), ga.getGroup().getGroupIcon(), ga.getGroup().getGroupName(), ga.getGroup().getGroupType(), ga.getGroup().getInviteLink());
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
}