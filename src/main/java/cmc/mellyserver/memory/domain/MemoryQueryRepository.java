package cmc.mellyserver.memory.domain;

import cmc.mellyserver.place.domain.Place;
import cmc.mellyserver.user.domain.User;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
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





}
