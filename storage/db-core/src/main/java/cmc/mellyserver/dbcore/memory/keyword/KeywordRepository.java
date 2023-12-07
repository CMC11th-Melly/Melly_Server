package cmc.mellyserver.dbcore.memory.keyword;

import java.util.List;
import java.util.Set;

import org.springframework.data.jpa.repository.JpaRepository;

public interface KeywordRepository extends JpaRepository<Keyword, Long> {

    List<Keyword> findAllByIdIn(Set<Long> keywordIds);
}
