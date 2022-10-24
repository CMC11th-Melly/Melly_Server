package cmc.mellyserver.placeScrap.domain;

import cmc.mellyserver.place.domain.Position;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PlaceScrapRepository extends JpaRepository<PlaceScrap,Long> {

    Optional<PlaceScrap> findByUserUserSeqAndPlacePosition(Long userSeq, Position position);
    Optional<PlaceScrap> findByPlaceIdAndUserUserSeq(Long placeId, Long userSeq);
    void deleteByUserUserSeqAndPlacePosition(Long userSeq,Position position);

}