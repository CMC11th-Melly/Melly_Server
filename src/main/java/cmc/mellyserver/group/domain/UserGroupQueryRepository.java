package cmc.mellyserver.group.domain;

import cmc.mellyserver.group.domain.enums.GroupType;
import cmc.mellyserver.memory.domain.Memory;
import cmc.mellyserver.memory.domain.MemorySearchDto;
import cmc.mellyserver.memory.domain.enums.OpenType;
import cmc.mellyserver.user.domain.User;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

import static cmc.mellyserver.group.domain.QUserGroup.*;
import static cmc.mellyserver.memory.domain.QMemory.memory;

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




}