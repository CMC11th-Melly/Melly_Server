package cmc.mellyserver.memory.domain;

import cmc.mellyserver.group.domain.enums.GroupType;
import cmc.mellyserver.memory.domain.enums.OpenType;
import cmc.mellyserver.place.domain.Place;
import cmc.mellyserver.user.domain.User;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.NumberExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

import static cmc.mellyserver.memory.domain.QMemory.*;
import static cmc.mellyserver.place.domain.QPlace.place;

@Repository
public class MemoryQueryRepository {


        private final EntityManager em;
        private final JPAQueryFactory query;

        // 인텔리제이 인식 오류, 무시하고 진행하면 된다.
        public MemoryQueryRepository(EntityManager em)
        {
            this.em = em;
            this.query = new JPAQueryFactory(em);
        }

        public List<Memory> searchMember(Long userSeq, String title)
        {
            return query.select(memory)
                    .from(memory)
                    .where(memory.user.userSeq.eq(userSeq), memory.title.contains(title))
                    .fetch();
        }

        public List<MemorySearchDto> searchMemoryName(Long userSeq, String memoryName)
        {
            return query.select(Projections.constructor(MemorySearchDto.class,memory.place.id,memory.title))
                    .from(memory)
                    .where(memory.user.userSeq.eq(userSeq),memory.title.contains(memoryName)).distinct().fetch();
        }

        public List<Memory> searchMemoryUserCreate(Long userSeq,Long placeId,String keyword, GroupType groupType, LocalDate createdDate){
            return query.select(memory)
                    .from(memory)

                    .where(
                            memory.place.id.eq(placeId),
                            memory.user.userSeq.eq(userSeq),
                            eqKeyword(keyword),
                            eqGroup(groupType),
                            eqCreatedDate(createdDate)

                            )
                    .fetch();
        }

        public List<Memory> searchMemoryOtherCreate(Long userSeq,Long placeId,String keyword, LocalDate createdDate){

            return query.select(memory)
                .from(memory)

                .where(
                        // 1. 그 장소에 메모리가 존재하는지 체크
                        memory.place.id.eq(placeId),
                        // 2. 지금 로그인한 유저의 메모리가 아니고,
                        memory.user.userSeq.ne(userSeq),
                        // 3. 이 메모리는 전체 공개로 공개가 됐다.
                        memory.openType.eq(OpenType.ALL),
                        eqKeyword(keyword),
                        eqCreatedDate(createdDate)
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
    private BooleanExpression eqCreatedDate(LocalDate createdDate)
    {

        if(createdDate == null)
        {
            return null;
        }

        return memory.createdDate.between(
                createdDate.atStartOfDay(),
                LocalDateTime.of(createdDate, LocalTime.of(23,59,59)));


    }






}
