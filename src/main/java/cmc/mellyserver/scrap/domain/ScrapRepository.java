package cmc.mellyserver.scrap.domain;

import cmc.mellyserver.place.domain.Place;
import cmc.mellyserver.place.domain.Position;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ScrapRepository extends JpaRepository<Scrap,Long> {

    Optional<Scrap> findByUserUserSeqAndPlacePosition(Long userSeq, Position position);
    Optional<Scrap> findByPlaceIdAndUserUserSeq(Long placeId,Long userSeq);
    void deleteByUserUserSeqAndPlacePosition(Long userSeq,Position position);
}