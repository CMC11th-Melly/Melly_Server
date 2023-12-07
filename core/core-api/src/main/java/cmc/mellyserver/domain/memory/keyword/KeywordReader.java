package cmc.mellyserver.domain.memory.keyword;

import java.util.List;
import java.util.Set;

import org.springframework.stereotype.Component;

import cmc.mellyserver.dbcore.memory.keyword.Keyword;
import cmc.mellyserver.dbcore.memory.keyword.KeywordRepository;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class KeywordReader {

    private final KeywordRepository keywordRepository;

    public List<Keyword> getKeywords(Set<Long> keywordIds) {
        return keywordRepository.findAllByIdIn(keywordIds);
    }
}
