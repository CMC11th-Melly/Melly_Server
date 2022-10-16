package cmc.mellyserver.memory.domain;

import cmc.mellyserver.group.domain.enums.GroupType;
import cmc.mellyserver.memory.domain.enums.OpenType;
import cmc.mellyserver.memory.presentation.dto.MemorySearchDto;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

import static cmc.mellyserver.memory.domain.QMemory.*;

@Repository
public class MemoryQueryRepository {


        private final EntityManager em;
        private final JPAQueryFactory query;

        public MemoryQueryRepository(EntityManager em)
        {
            this.em = em;
            this.query = new JPAQueryFactory(em);
        }


        public List<MemorySearchDto> searchMemoryName(Long userSeq, String memoryName)
        {
            return query.select(Projections.constructor(MemorySearchDto.class,memory.place.id,memory.title))
                    .from(memory)
                    .where(memory.user.userSeq.eq(userSeq),memory.title.contains(memoryName)).distinct().fetch();
        }

    /**
     *
     * @param userSeq 유저 식별값
     * @param placeId 장소 아이디
     * @param keyword 메모리 작성시 설정한 키워드
     * @param groupType 메모리가 속한 그룹의 타입
     * @param visitiedDate 메모리의 장소에 방문한 날짜
     */
        public List<Memory> searchMemoryUserCreate(Long userSeq,Long placeId,String keyword, GroupType groupType, LocalDate visitiedDate){

            return query.select(memory)
                    .from(memory)
                    .where(
                            memory.place.id.eq(placeId),
                            memory.user.userSeq.eq(userSeq),
                            eqKeyword(keyword),
                            eqGroup(groupType),
                            eqVisitiedDate(visitiedDate)
                            )
                    .fetch();
        }

        public List<Memory> searchMemoryOtherCreate(Long userSeq,Long placeId,String keyword, LocalDate visitiedDate){

            return query.select(memory)
                .from(memory)

                .where(
                        // 1. 그 장소에 메모리가 존재하는지 체크
                        memory.place.id.eq(placeId),
                        // 2. 지금 로그인한 유저의 메모리가 아니고,
                        memory.user.userSeq.ne(userSeq),
                        // 3. 이 메모리는 전체 공개로 공개가 됐다.
                        // 4. 만약 그룹을 하나라도 선택했으면 OpenType.GROUP으로 설정
                        memory.openType.eq(OpenType.ALL),
                        eqKeyword(keyword),
                        eqVisitiedDate(visitiedDate)
                )
                .fetch();
    }

    private BooleanExpression eqKeyword(String keyword)
    {
        if(keyword == null || keyword.isEmpty())
        {
            return null;
        }

        return memory.keyword.contains(keyword);
    }

    private BooleanExpression eqGroup(GroupType groupType)
    {
        if(groupType == null)
        {
            return null;
        }

        return memory.groupInfo.groupType.eq(groupType);
    }

    //  TODO : LocalDateTime을 LocalDate로 변환해주는 get 함수 필요!
    private BooleanExpression eqVisitiedDate(LocalDate visitiedDate)
    {

        if(visitiedDate == null)
        {
            return null;
        }

        return memory.visitedDate.between(
                visitiedDate.atStartOfDay(),
                LocalDateTime.of(visitiedDate, LocalTime.of(23,59,59)));


    }






}
