package cmc.mellyserver.mellycore.group.domain.repository;

import cmc.mellyserver.mellycommon.enums.OpenType;
import cmc.mellyserver.mellycore.common.util.jpa.QueryDslUtil;
import cmc.mellyserver.mellycore.group.domain.repository.dto.GroupLoginUserParticipatedResponseDto;
import cmc.mellyserver.mellycore.memory.domain.repository.dto.ImageDto;
import cmc.mellyserver.mellycore.memory.domain.repository.dto.KeywordResponseDto;
import cmc.mellyserver.mellycore.memory.domain.repository.dto.MemoryResponseDto;
import com.querydsl.core.types.Order;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.SliceImpl;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static cmc.mellyserver.mellycore.group.domain.QGroupAndUser.groupAndUser;
import static cmc.mellyserver.mellycore.group.domain.QUserGroup.userGroup;
import static cmc.mellyserver.mellycore.memory.domain.QMemory.memory;
import static cmc.mellyserver.mellycore.memory.domain.QMemoryImage.memoryImage;
import static cmc.mellyserver.mellycore.user.domain.QUser.user;
import static org.springframework.util.ObjectUtils.isEmpty;

@Repository
public class UserGroupQueryRepository {

    private final EntityManager em;
    private final JPAQueryFactory query;

    public UserGroupQueryRepository(EntityManager em) {
        this.em = em;
        this.query = new JPAQueryFactory(em);
    }


    @Transactional(readOnly = true) // 완료
    public List<GroupLoginUserParticipatedResponseDto> getGroupListLoginUserParticipate(Long userSeq) {

        return query.select(Projections.constructor(GroupLoginUserParticipatedResponseDto.class, userGroup.id, userGroup.groupIcon, userGroup.groupName, userGroup.groupType))
                .from(groupAndUser)
                .innerJoin(groupAndUser.group, userGroup)
                .innerJoin(groupAndUser.user, user)
                .where(eqUser(userSeq))
                .fetch();
    }

    private BooleanExpression eqUser(Long userSeq) {
        if (userSeq == null || userSeq == -1) {
            return null;
        }
        return groupAndUser.user.userSeq.eq(userSeq);
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


//            List<User> users = map.get(getUserGroupResponse.getGroupId());
//
//            List<UserDto> collect = users.stream()
//                    .map(u -> new UserDto(u.getUserSeq(), u.getProfileImage(), u.getNickname(),
//                            u.getUserSeq().equals(userSeq)))
//                    .collect(Collectors.toList());
//
//            getUserGroupResponse.setUsers(collect);
//