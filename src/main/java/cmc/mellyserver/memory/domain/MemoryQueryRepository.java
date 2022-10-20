package cmc.mellyserver.memory.domain;

import cmc.mellyserver.group.domain.enums.GroupType;
import cmc.mellyserver.memory.domain.enums.OpenType;
import cmc.mellyserver.memory.presentation.dto.MemorySearchDto;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.geolatte.geom.M;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.SliceImpl;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

import static cmc.mellyserver.memory.domain.QMemory.*;

@Repository
public class MemoryQueryRepository {


    private final EntityManager em;
    private final JPAQueryFactory query;

    public MemoryQueryRepository(EntityManager em) {
        this.em = em;
        this.query = new JPAQueryFactory(em);
    }


    public List<MemorySearchDto> searchMemoryName(Long userSeq, String memoryName) {
        return query.select(Projections.constructor(MemorySearchDto.class, memory.place.id, memory.title))
                .from(memory)
                .where(memory.user.userSeq.eq(userSeq), memory.title.contains(memoryName)).distinct().fetch();
    }

    /**
     * @param userSeq      유저 식별값
     * @param placeId      장소 아이디
     * @param keyword      메모리 작성시 설정한 키워드
     * @param groupType    메모리가 속한 그룹의 타입
     * @param visitiedDate 메모리의 장소에 방문한 날짜
     * 만약 groupType이 null이나 ALL로 들어가면 조건 아예 없어버림. size만 처음에 10 넣고, 다음부터 마지막 데이터 가져오기
     */
    public Slice<Memory> searchMemoryUserCreate(Long lastMemoryId, Pageable pageable, Long userSeq, Long placeId, String keyword, GroupType groupType, String visitiedDate) {

        List<Memory> results = query.select(memory)
                .from(memory)
                .where(
                        ltMemoryId(lastMemoryId),  // 메모리 id를 커서로 사용해서 페이징 -> 인덱스 사용
                        eqPlace(placeId),          // 특정 장소에 대한 메모리를 가져옴
                        eqUserSeq(userSeq),  // 특정 장소에 대한 메모리들 중 유저가 작성한 메모리 가져옴
                        eqKeyword(keyword),      // 해당 키워드가 필요한 메모리
                        eqGroup(groupType),      // 특정 groupType에 포함되는 메모리
                        eqVisitiedDate(visitiedDate)  // 특정 날짜에 속하는 메모리
                ).orderBy(memory.id.desc())          // memoryId 순서로 내림차순
                .limit(pageable.getPageSize() + 1)
                .fetch();
        return checkLastPage(pageable, results);
    }

    public Slice<Memory> searchMemoryOtherCreate(Long lastId, Pageable pageable, Long userSeq, Long placeId, String keyword, String visitiedDate) {

        List<Memory> results = query.select(memory)
                .from(memory)

                .where(
                        ltMemoryId(lastId),
                        eqPlace(placeId),
                        neUserSeq(userSeq),

                        // 상대방 메모리 중에 전체 공개인것만 가져오기
                        memory.openType.eq(OpenType.ALL),
                        eqKeyword(keyword),
                        eqVisitiedDate(visitiedDate)
                ).orderBy(memory.id.desc())
                .limit(pageable.getPageSize() + 1)
                .fetch();

        return checkLastPage(pageable, results);
    }


    private BooleanExpression eqKeyword(String keyword) {

        System.out.println("keyword = " + keyword);
        if (keyword == null || keyword.isEmpty()) {
            return null;
        }

        return memory.keyword.eq(keyword);
    }


    private BooleanExpression eqGroup(GroupType groupType) {

        if (groupType == null || groupType == GroupType.ALL) {
            return null;
        }


        return memory.groupInfo.groupType.eq(groupType);
    }


    private BooleanExpression eqVisitiedDate(String visitiedDate) {

        if (visitiedDate == null || visitiedDate.isEmpty()) {
            return null;
        }

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMddHHmm");
        LocalDate visited = LocalDate.parse(visitiedDate, formatter);

        return memory.visitedDate.between(
                visited.atStartOfDay(),
                LocalDateTime.of(visited, LocalTime.of(23, 59, 59)));


    }


    private BooleanExpression eqPlace(Long placeId) {
        if (placeId == null) {
            return null;
        }
        return memory.place.id.eq(placeId);
    }

    private BooleanExpression eqUserSeq(Long userSeq)
    {
        if(userSeq == null)
        {
            return null;
        }
        return memory.user.userSeq.eq(userSeq);
    }

    private BooleanExpression neUserSeq(Long userSeq)
    {
        if(userSeq == null)
        {
            return null;
        }
        return memory.user.userSeq.ne(userSeq);
    }


    private BooleanExpression ltMemoryId(Long memoryId) {

        if (memoryId == null || memoryId == -1) {
            return null;
        }

        return memory.id.lt(memoryId);
    }


    private Slice<Memory> checkLastPage(Pageable pageable, List<Memory> results) {

        boolean hasNext = false;

        // 조회한 결과 개수가 요청한 페이지 사이즈보다 크면 뒤에 더 있음, next = true
        if (results.size() > pageable.getPageSize()) {
            hasNext = true;
            results.remove(pageable.getPageSize());
        }

        return new SliceImpl<>(results, pageable, hasNext);
    }


}
